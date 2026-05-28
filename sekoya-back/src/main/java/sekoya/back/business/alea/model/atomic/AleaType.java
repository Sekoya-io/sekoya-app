package sekoya.back.business.alea.model.atomic;

import sekoya.back.business.common.model.atomic.Indicateur;

public enum AleaType {
  MODIFICATION_TEMPERATURES_AIR(Indicateur.ATAV),
  STRESS_THERMIQUE(Indicateur.ATXQ90),
  VARIABILITE_TEMPERATURES(Indicateur.ATRAV),
  VAGUE_CHALEUR(Indicateur.ATXHWD),
  VAGUE_FROID_OU_GEL(Indicateur.ATNFD),
  FEU_FORET(Indicateur.AIFM20),
  TEMPETES(Indicateur.AFFQ98),
  MODIFICATION_REGIMES_PRECIPITATIONS_PLUIE(Indicateur.ARRR),
  //  EVOLUTION_PRECIPITATIONS_NEIGEUSES_MONTAGNE(Indicateur.ARRSN),
  //  VARIATION_PRECIPITATIONS_OU_HYDROLOGIE(Indicateur.ARQ05),
  //  STRESS_HYDRIQUE(Indicateur.ARQ05),
  //  INTRUSION_SALINE(Indicateur.ARQ05),
  //  HAUSSE_NIVEAU_MER(null),
  SECHERESSE(Indicateur.APXCDD),
  FORTES_PRECIPITATIONS_PLUIE(Indicateur.APN20MM),
  //  INONDATION_FLUVIALE(Indicateur.ARQ95),
  INONDATION_PLUVIALE(Indicateur.ARPQ99),
  INONDATION_REMONTEE_NAPPE(Indicateur.ARRR),
  INONDATION_COTIERE(null),
  RETRAIT_GONFLEMENT_ARGILES(Indicateur.APXCDD),
  GLISSEMENT_TERRAIN(Indicateur.ARPQ99),
  AFFAISSEMENT(Indicateur.ARPQ99);

  private final Indicateur indicateur;

  AleaType(Indicateur indicateur) {
    this.indicateur = indicateur;
  }

  public Indicateur getIndicateur() {
    return indicateur;
  }
}
