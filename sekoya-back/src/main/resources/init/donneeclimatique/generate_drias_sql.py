#!/usr/bin/env python3
"""
Usage:
    python generate_drias_sql.py FICHIER... [-d dossier_sortie]
    python generate_drias_sql.py *.txt -d ./sql
"""

from __future__ import annotations

import argparse
import csv
import re
import sys
from dataclasses import dataclass, field
from decimal import Decimal, InvalidOperation
from pathlib import Path
from typing import Iterator, TextIO

# --------------------------------------------------------------------------- #
# Constantes de mapping (alignées sur les CHECK de l'init hbm2ddl)
# --------------------------------------------------------------------------- #

BATCH_SIZE = 1000

# Indicateurs autorisés par le schéma (colonne `indicateur`).
INDICATEURS_VALIDES: frozenset[str] = frozenset({
    "AFFQ98", "AIFM20", "APN20MM", "APXCDD", "ARPQ99", "ARQ05", "ARQ95",
    "ARRR", "ARRSN", "ATAV", "ATNFD", "ATRAV", "ATXHWD", "ATXQ90",
})

# Contexte -> Enum Scenario
SCENARIO_PAR_CONTEXTE: dict[str, str] = {
    "RCP4.5": "RCP_4_5",
    "RCP8.5": "RCP_8_5",
}

# Période -> Enum Horizon
HORIZON_PAR_PERIODE: dict[str, str] = {
    "H1": "ANNEE_2035",
    "H2": "ANNEE_2055",
}

# Saison (numéro) -> Enum Saison. NULL pour les indices annuels.
SAISON_PAR_NUMERO: dict[str, str] = {
    "1": "HIVER",
    "2": "PRINTEMPS",
    "3": "ETE",
    "4": "AUTOMNE",
}

# Colonnes fixes (non indicateur) du format DRIAS.
COLONNES_FIXES: frozenset[str] = frozenset({
    "Point", "Latitude", "Longitude", "Contexte", "Période", "Saison",
})

# Séquences déclarées dans l'init hbm2ddl.
SEQ_POINT = "PointGeographique_id_seq"
SEQ_DCB = "DonneeClimatiqueBrute_id_seq"

# SRID WGS84 (cohérent avec une colonne PostGIS `geography`).
SRID = 4326


# --------------------------------------------------------------------------- #
# Modèle de données
# --------------------------------------------------------------------------- #

@dataclass(frozen=True, slots=True)
class EnTete:
    """Métadonnées extraites de l'en-tête `#` et du nom de fichier."""
    colonnes: list[str]
    indicateurs: list[str]
    type_indice_annuel: bool
    scenario_fichier: str | None  # déduit du nom de fichier si absent des lignes


@dataclass(frozen=True, slots=True)
class Point:
    id_drias: int
    lat: float
    lon: float


@dataclass(slots=True)
class DonneeBrute:
    id_drias: int
    indicateur: str
    scenario: str
    horizon: str
    saison: str | None
    ecart: Decimal


@dataclass(slots=True)
class Groupe:
    """Données accumulées pour un jeu d'indicateurs donné."""
    indicateurs: tuple[str, ...]
    fichiers: list[str] = field(default_factory=list)
    points: dict[int, Point] = field(default_factory=dict)  # idDrias -> Point
    donnees: list[DonneeBrute] = field(default_factory=list)

    def ajouter_point(self, p: Point) -> None:
        # Premier vu gagne; les coordonnées d'un même idDrias sont identiques
        # entre fichiers d'un même groupe.
        self.points.setdefault(p.id_drias, p)


# --------------------------------------------------------------------------- #
# Parsing
# --------------------------------------------------------------------------- #

_RE_FORMAT = re.compile(r"^#\s*(Point\s*;.*)$")
_RE_TYPE_ANNUEL = re.compile(r"type\s+d['’]?indice\s*:\s*annuel", re.IGNORECASE)
_RE_SCENARIO_FICHIER = re.compile(r"RCP(\d)[._](\d)", re.IGNORECASE)


def deduire_scenario_depuis_nom(nom_fichier: str) -> str | None:
    """Déduit l'enum Scenario depuis le nom de fichier (ex: RCP4_5 -> RCP_4_5)."""
    m = _RE_SCENARIO_FICHIER.search(nom_fichier)
    if not m:
        return None
    return f"RCP_{m.group(1)}_{m.group(2)}"


def _decoupe_csv(ligne: str) -> list[str]:
    """Découpe une seule ligne `;` via le module csv (gère quoting/échappement).

    `csv.reader` attend un itérable de lignes; on lui en passe une seule et on
    récupère l'unique enregistrement. Le `;` final éventuel produit un dernier
    champ vide, retiré ici.
    """
    enregistrement = next(csv.reader([ligne], delimiter=";"), [])
    champs = [c.strip() for c in enregistrement]
    if champs and champs[-1] == "":  # `;` final -> colonne fantôme
        champs.pop()
    return champs


def _lignes_donnees(f: Iterator[str]) -> Iterator[tuple[int, str]]:
    """Filtre les lignes de commentaire `#` et vides, renvoie (num_ligne, ligne)."""
    for num_ligne, ligne in enumerate(f, start=1):
        ligne = ligne.rstrip("\n")
        if not ligne or ligne.startswith("#"):
            continue
        yield num_ligne, ligne


def lire_entete(chemin: Path) -> EnTete:
    """Lit l'en-tête `#` pour extraire colonnes, indicateurs et type d'indice."""
    colonnes: list[str] = []
    type_annuel = False

    with chemin.open(encoding="utf-8") as f:
        for ligne in f:
            if not ligne.startswith("#"):
                break  # fin de l'en-tête
            if _RE_TYPE_ANNUEL.search(ligne):
                type_annuel = True
            m = _RE_FORMAT.match(ligne.rstrip("\n"))
            if m:
                colonnes = [c for c in _decoupe_csv(m.group(1)) if c]

    if not colonnes:
        raise ValueError(
            f"{chemin.name}: ligne de format "
            "(`# Point;Latitude;Longitude;...`) introuvable dans l'en-tête."
        )

    indicateurs = [c for c in colonnes if c not in COLONNES_FIXES]
    inconnus = [c for c in indicateurs if c not in INDICATEURS_VALIDES]
    if inconnus:
        raise ValueError(
            f"{chemin.name}: colonne(s) indicateur non reconnue(s): "
            f"{', '.join(inconnus)}. Indicateurs valides: "
            f"{', '.join(sorted(INDICATEURS_VALIDES))}."
        )
    if not indicateurs:
        raise ValueError(f"{chemin.name}: aucune colonne d'indicateur détectée.")

    return EnTete(
        colonnes=colonnes,
        indicateurs=indicateurs,
        type_indice_annuel=type_annuel,
        scenario_fichier=deduire_scenario_depuis_nom(chemin.name),
    )


def _convertir_ecart(brut: str) -> Decimal:
    try:
        return Decimal(brut.replace(",", "."))
    except InvalidOperation as exc:
        raise ValueError(f"écart invalide: {brut!r}") from exc


def parser_fichier(chemin: Path, entete: EnTete, groupe: Groupe) -> None:
    """Parse les lignes de données, alimente points et données du groupe."""
    index = {nom: i for i, nom in enumerate(entete.colonnes)}
    i_point = index["Point"]
    i_lat = index["Latitude"]
    i_lon = index["Longitude"]
    i_ctx = index.get("Contexte")
    i_periode = index["Période"]
    i_saison = index.get("Saison")
    indices_indicateurs = [(ind, index[ind]) for ind in entete.indicateurs]

    with chemin.open(encoding="utf-8") as f:
        for num_ligne, ligne in _lignes_donnees(f):
            champs = _decoupe_csv(ligne)

            try:
                id_drias = int(champs[i_point])
                lat = float(champs[i_lat])
                lon = float(champs[i_lon])

                contexte = champs[i_ctx] if i_ctx is not None else None
                scenario = (
                    SCENARIO_PAR_CONTEXTE.get(contexte) if contexte else None
                ) or entete.scenario_fichier
                if scenario is None:
                    raise ValueError(
                        f"scénario introuvable (contexte={contexte!r}, "
                        "non déductible du nom de fichier)"
                    )

                periode = champs[i_periode]
                horizon = HORIZON_PAR_PERIODE.get(periode)
                if horizon is None:
                    raise ValueError(f"période inconnue: {periode!r}")

                if entete.type_indice_annuel or i_saison is None:
                    saison = None
                else:
                    numero = champs[i_saison]
                    saison = SAISON_PAR_NUMERO.get(numero)
                    if saison is None:
                        raise ValueError(f"saison inconnue: {numero!r}")
            except (IndexError, ValueError) as exc:
                raise ValueError(
                    f"{chemin.name}:{num_ligne}: {exc} | ligne={ligne!r}"
                ) from exc

            groupe.ajouter_point(Point(id_drias=id_drias, lat=lat, lon=lon))

            for indicateur, idx in indices_indicateurs:
                brut = champs[idx]
                if brut == "":
                    continue  # valeur manquante -> pas de donnée
                groupe.donnees.append(DonneeBrute(
                    id_drias=id_drias,
                    indicateur=indicateur,
                    scenario=scenario,
                    horizon=horizon,
                    saison=saison,
                    ecart=_convertir_ecart(brut),
                ))


# --------------------------------------------------------------------------- #
# Génération SQL
# --------------------------------------------------------------------------- #

def _ecrire_par_batch(
    sortie: TextIO,
    en_tete: str,
    pied: str,
    lignes: list[str],
) -> None:
    """Écrit `en_tete` + lignes VALUES (paquets de BATCH_SIZE) + `pied`.

    `en_tete` contient l'INSERT/SELECT jusqu'au mot-clé `VALUES\n` inclus;
    `pied` contient ce qui suit le bloc VALUES (alias de colonnes, JOIN,
    ON CONFLICT, `;`...). Le typage des colonnes est assuré par des casts
    explicites dans le SELECT de `en_tete`, pas par l'inférence du VALUES.
    """
    for debut in range(0, len(lignes), BATCH_SIZE):
        paquet = lignes[debut:debut + BATCH_SIZE]
        sortie.write(en_tete)
        sortie.write(",\n".join(paquet))
        sortie.write(pied)


def ecrire_points(sortie: TextIO, groupe: Groupe) -> int:
    # Le VALUES ne porte que la donnée brute; nextval() et la construction
    # PostGIS sont factorisés une seule fois dans le SELECT.
    lignes = [
        f"({p.id_drias}, {p.lon!r}, {p.lat!r})"
        for id_drias in sorted(groupe.points)
        if (p := groupe.points[id_drias])
    ]
    if not lignes:
        return 0

    sortie.write("-- PointGeographique (référentiel commun, idempotent)\n")
    # Casts dans le SELECT (et non via l'inférence du VALUES): coordonnées en
    # float8 pour ST_MakePoint quelles que soient les valeurs (ex. lon entier).
    en_tete = (
        "INSERT INTO PointGeographique (id, idDrias, localisation, longitude, latitude))\n"
        f"SELECT nextval('{SEQ_POINT}'), v.idDrias,\n"
        "       ST_SetSRID(ST_MakePoint(v.lon::float8, v.lat::float8), "
        f"{SRID})::geography\n"
        "       v.lon::decimal(9,6),\n"
        "       v.lat::decimal(8,6)\n""
        "FROM (VALUES\n"
    )
    pied = (
        "\n) AS v(idDrias, lon, lat)\n"
        "ON CONFLICT (idDrias) DO NOTHING;\n"
    )
    _ecrire_par_batch(sortie, en_tete, pied, lignes)
    sortie.write("\n")
    return len(lignes)


def ecrire_donnees(sortie: TextIO, groupe: Groupe) -> int:
    # idem: nextval() factorisé; le point est résolu par un JOIN unique sur
    # PointGeographique plutôt qu'une sous-requête par ligne.
    lignes: list[str] = []
    for d in groupe.donnees:
        saison = "NULL" if d.saison is None else f"'{d.saison}'"
        lignes.append(
            f"({d.id_drias}, {d.ecart}, '{d.horizon}', "
            f"'{d.indicateur}', {saison}, '{d.scenario}')"
        )
    if not lignes:
        return 0

    sortie.write("-- DonneeClimatiqueBrute\n")
    # Casts dans le SELECT: ecart en numeric, et saison en varchar (sinon une
    # colonne 100% NULL est résolue en `text` -> cast implicite vers l'enum
    # fragile; ici on s'appuie sur le cast implicite varchar->enum du schéma).
    en_tete = (
        "INSERT INTO DonneeClimatiqueBrute\n"
        "       (id, ecart, horizon, indicateur, saison, scenario, "
        "pointGeographique_id)\n"
        f"SELECT nextval('{SEQ_DCB}'), v.ecart::numeric(5,2), v.horizon, "
        "v.indicateur, v.saison::varchar, v.scenario, pg.id\n"
        "FROM (VALUES\n"
    )
    pied = (
        "\n) AS v(idDrias, ecart, horizon, indicateur, saison, scenario)\n"
        "JOIN PointGeographique pg ON pg.idDrias = v.idDrias;\n"
    )
    _ecrire_par_batch(sortie, en_tete, pied, lignes)
    sortie.write("\n")
    return len(lignes)


def ecrire_script_groupe(groupe: Groupe, chemin_sortie: Path) -> tuple[int, int]:
    with chemin_sortie.open("w", encoding="utf-8") as sortie:
        sortie.write(
            f"-- Indicateurs: {', '.join(groupe.indicateurs)}\n"
        )
        sortie.write(
            f"-- Fichiers source: {', '.join(groupe.fichiers)}\n\n"
        )
        sortie.write("BEGIN;\n\n")
        nb_points = ecrire_points(sortie, groupe)
        nb_donnees = ecrire_donnees(sortie, groupe)
        sortie.write("COMMIT;\n")
    return nb_points, nb_donnees


# --------------------------------------------------------------------------- #
# Programme principal
# --------------------------------------------------------------------------- #

def regrouper_par_indicateurs(fichiers: list[Path]) -> dict[tuple[str, ...], Groupe]:
    groupes: dict[tuple[str, ...], Groupe] = {}
    for chemin in fichiers:
        entete = lire_entete(chemin)
        # Clé = jeu d'indicateurs, ordre préservé tel que dans le fichier.
        cle = tuple(entete.indicateurs)
        groupe = groupes.get(cle)
        if groupe is None:
            groupe = Groupe(indicateurs=cle)
            groupes[cle] = groupe
        groupe.fichiers.append(chemin.name)
        parser_fichier(chemin, entete, groupe)
    return groupes


def nom_fichier_groupe(indicateurs: tuple[str, ...]) -> str:
    return "insert_" + "_".join(indicateurs) + ".sql"


def main(argv: list[str] | None = None) -> int:
    parseur = argparse.ArgumentParser(
        description="Génère un script SQL par jeu d'indicateurs "
                    "(PointGeographique + DonneeClimatiqueBrute) depuis des "
                    "extractions DRIAS.",
    )
    parseur.add_argument(
        "fichiers", nargs="+", type=Path, help="Fichier(s) d'extraction DRIAS",
    )
    parseur.add_argument(
        "-d", "--dossier-sortie", type=Path, default=Path("."),
        help="Dossier où écrire les scripts SQL (défaut: dossier courant).",
    )
    args = parseur.parse_args(argv)

    for f in args.fichiers:
        if not f.is_file():
            print(f"Erreur: fichier introuvable: {f}", file=sys.stderr)
            return 1

    args.dossier_sortie.mkdir(parents=True, exist_ok=True)

    try:
        groupes = regrouper_par_indicateurs(args.fichiers)
    except ValueError as exc:
        print(f"Erreur: {exc}", file=sys.stderr)
        return 1

    for indicateurs, groupe in groupes.items():
        chemin = args.dossier_sortie / nom_fichier_groupe(indicateurs)
        nb_p, nb_d = ecrire_script_groupe(groupe, chemin)
        print(
            f"{chemin.name}: {nb_p} points, {nb_d} données "
            f"(indicateurs: {', '.join(indicateurs)})",
            file=sys.stderr,
        )

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
