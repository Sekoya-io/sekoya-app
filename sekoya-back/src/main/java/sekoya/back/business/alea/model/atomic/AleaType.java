package sekoya.back.business.alea.model.atomic;

import sekoya.back.business.common.model.atomic.Indicateur;

public enum AleaType {
  MODIFICATION_TEMPERATURES_AIR(Indicateur.ATAV, "fa-temperature-half"),
  STRESS_THERMIQUE(Indicateur.ATXQ90, "fa-temperature-high"),
  VARIABILITE_TEMPERATURES(Indicateur.ATRAV, "fa-temperature-low"),
  VAGUE_CHALEUR(Indicateur.ATXHWD, "fa-sun"),
  VAGUE_FROID_OU_GEL(Indicateur.ATNFD, "fa-snowflake"),
  FEU_FORET(Indicateur.AIFM20, "fa-fire"),
  TEMPETES(Indicateur.AFFQ98, "fa-wind"),
  MODIFICATION_REGIMES_PRECIPITATIONS_PLUIE(Indicateur.ARRR, "fa-cloud-rain"),
  //  EVOLUTION_PRECIPITATIONS_NEIGEUSES_MONTAGNE(Indicateur.ARRSN, ""),
  //  VARIATION_PRECIPITATIONS_OU_HYDROLOGIE(Indicateur.ARQ05, ""),
  //  STRESS_HYDRIQUE(Indicateur.ARQ05, ""),
  //  INTRUSION_SALINE(Indicateur.ARQ05, ""),
  //  HAUSSE_NIVEAU_MER(null, ""),
  SECHERESSE(Indicateur.APXCDD, "fa-sun-plant-wilt"),
  FORTES_PRECIPITATIONS_PLUIE(Indicateur.APN20MM, "fa-cloud-showers-heavy"),
  //  INONDATION_FLUVIALE(Indicateur.ARQ95, ""),
  INONDATION_PLUVIALE(Indicateur.ARPQ99, "fa-cloud-showers-water"),
  INONDATION_REMONTEE_NAPPE(Indicateur.ARRR, "fa-arrow-up-from-ground-water"),
  INONDATION_COTIERE(null, "fa-water"),
  RETRAIT_GONFLEMENT_ARGILES(Indicateur.APXCDD, "fa-house-crack"),
  GLISSEMENT_TERRAIN(Indicateur.ARPQ99, "fa-hill-rockslide"),
  AFFAISSEMENT(Indicateur.ARPQ99, "fa-lines-leaning");

  private final Indicateur indicateur;

  private final String iconCssClass;

  AleaType(Indicateur indicateur, String iconCssClass) {
    this.indicateur = indicateur;
    this.iconCssClass = iconCssClass;
  }

  public Indicateur getIndicateur() {
    return indicateur;
  }

  public String getIconCssClass() {
    return iconCssClass;
  }
}
