package sekoya.back.business.referencedata.model.atomic;

import java.util.List;

public enum CommuneTypeInsee {
  COMMUNE,
  ARRONDISSEMENT_MUNICIPAL,
  COMMUNE_DELEGUEE,
  COMMUNE_ASSOCIEE,
  COMMUNE_COMER;

  public static List<CommuneTypeInsee> principaux() {
    return List.of(COMMUNE, ARRONDISSEMENT_MUNICIPAL, COMMUNE_COMER);
  }

  public static List<CommuneTypeInsee> secondaires() {
    return List.of(COMMUNE_DELEGUEE, COMMUNE_ASSOCIEE);
  }
}
