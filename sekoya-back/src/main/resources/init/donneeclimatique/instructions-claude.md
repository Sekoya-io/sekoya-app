# Objectif

Générer un script Python d'extraction des données pour générer les insert into sql pour les tâbles `PointGeographique` et `DonneeClimatiqueBrute`.
A partir de fichiers contenant du CSV et des infos de mappings.

Le script Python devra sortir 3 fichiers pour les 3 regroupements suivants :
* AIFM20
* ATNFD_ATXHWD_ARRR
* ATAV_ATRAV_ATXQ90_APN20MM_APXCDD_AFFQ98_ARPQ99

Chaque groupe est composé de 2 fichiers, un pour RCP 4.5 et un pour RCP 8.5.

# Input

* Script SQL du MDD de l'application.

# Table `PointGeographique`

* idDrias => colonne "Point"
* localisation => à générer à partir des colonnes "Latitude" et "Longitude"

Table sert de référentiel. On va avoir plusieurs fichiers / csv, si on tombe sur un point géographique qui n'existe pas 
on veut ajouter ce nouveau point. Sinon il faudra réutiliser un point déjà présent.

# Table `DonneeClimatiqueBrute`

* pointGeographique => Pointer vers un `PointGeographique` existant, le créer s'il n'existe pas déjà.
* indicateur => Enum `Indicateur`
* scenario => Enum `Scenario`. Colonne "Contexte". RCP4.5 = `RCP_4_5`, RCP8.5 = `RCP_8_5`. Info présente dans le fichier ou dans le titre du fichier.
* horizon => Enum `Horizon`. Colonne "Période". H1 = `ANNEE_2035`, H2 = `ANNEE_2055`.
* saison => Enum `Saison`. Colonne "Saison". 1 = `HIVER`, 2 = `PRINTEMPS`, 3 = `ETE`, 4 = `AUTOMNE`. Ne concerne pas les données climatiques de type d'indice annuel.
* ecart => Colonne du code indicateur. Des fichiers peuvent avoir qu'une colonne de type indicateur. D'autres fichiers peuvent avoir N colonnes de types indicateurs, il faudra alors bien N données climatiques, avec le bon indicateur et la valeur associée.

# Indications techniques

* Réduire la taille du fichier. Autant pour sa manipulation que pour te temps d'exécution du sql. Faire des insert values de 1000 lignes.
* Python 3.14.
* Code clean, robuste et pérenne.
* Exemple de sql insert souhaité :

```sql
INSERT INTO PointGeographique (...)
SELECT nextval('PointGeographique_id_seq'), v.XXX,
       ST_SetSRID(ST_MakePoint(v.XXX::float8, v.XXX::float8), 4326)::geography
FROM (VALUES
-- ...
) AS v(...)
ON CONFLICT (idDrias) DO NOTHING;
```
