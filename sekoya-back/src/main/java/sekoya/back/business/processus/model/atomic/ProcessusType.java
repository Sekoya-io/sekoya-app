package sekoya.back.business.processus.model.atomic;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum ProcessusType {
  FOURNISSEUR_1(ProcessusThematique.APPROVISIONNEMENTS, "fa-handshake"),
  FOURNISSEUR_2(ProcessusThematique.APPROVISIONNEMENTS, "fa-truck-ramp-box"),
  FOURNISSEUR_3(ProcessusThematique.APPROVISIONNEMENTS, "fa-boxes-stacked"),
  FOURNISSEUR_4(ProcessusThematique.APPROVISIONNEMENTS, "fa-dolly"),
  INTEGRITE_BATIMENTS(ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS, "fa-building"),
  INTEGRITE_ENTREPOTS_STOCKS(ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS, "fa-warehouse"),
  PRATICABILITE_VOIRIE_INTERNE_ZONES_EXTERIEURES(
      ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS, "fa-road"),
  INTEGRITE_FONCTIONNEMENT_EQUIPEMENTS_PRODUCTION(
      ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS, "fa-gears"),
  INTEGRITE_FONCTIONNEMENT_EQUIPEMENTS_ALIMENTATION_ELECTRIQUE(
      ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS, "fa-plug"),
  RESEAUX_CENTRES_DONNEES_SITE(ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS, "fa-server"),
  INTEGRITE_FONCTIONNEMENT_EQUIPEMENTS_PRODUCTION_FROID(
      ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS, "fa-snowflake"),
  MAINTIEN_CONDITIONS_PRODUCTION_INTERIEUR(ProcessusThematique.OPERATIONS_PROCESSUS, "fa-industry"),
  MAINTIEN_CONDITIONS_PRODUCTION_EXTERIEUR(
      ProcessusThematique.OPERATIONS_PROCESSUS, "fa-cloud-sun"),
  MAINTIEN_CONDITIONS_TRAVAIL_INTERIEUR(ProcessusThematique.OPERATIONS_PROCESSUS, "fa-house-user"),
  MAINTIEN_CONDITIONS_TRAVAIL_EXTERIEUR(
      ProcessusThematique.OPERATIONS_PROCESSUS, "fa-helmet-safety"),
  EXPLOITATION_RESSOURCES_NATURELLES_AGRICOLES_HALIEUTIQUES(
      ProcessusThematique.OPERATIONS_PROCESSUS, "fa-wheat-awn"),
  EXPLOITATION_RESSOURCES_NATURELLES_TOURISTIQUES(
      ProcessusThematique.OPERATIONS_PROCESSUS, "fa-umbrella-beach"),
  STOCKAGE_EVACUATION_EAUX_USEES(ProcessusThematique.OPERATIONS_PROCESSUS, "fa-water"),
  STOCKAGE_EVACUATION_DECHETS(ProcessusThematique.OPERATIONS_PROCESSUS, "fa-dumpster"),
  ELECTRICITE(ProcessusThematique.ENERGIE_TELECOMS, "fa-bolt"),
  GAZ(ProcessusThematique.ENERGIE_TELECOMS, "fa-fire-flame-simple"),
  RESEAU_CHALEUR_FROID(ProcessusThematique.ENERGIE_TELECOMS, "fa-temperature-half"),
  APPROVISIONNEMENT_STOCK_COMBUSTIBLE_SITE(ProcessusThematique.ENERGIE_TELECOMS, "fa-gas-pump"),
  DISPONIBILITE_QUALITE_RESEAUX_TELECOMS_INTERNET(ProcessusThematique.ENERGIE_TELECOMS, "fa-wifi"),
  APPROVISIONNEMENT_EAU_PRINCIPAL_QUANTITE(ProcessusThematique.EAU, "fa-droplet"),
  APPROVISIONNEMENT_EAU_PRINCIPAL_QUALITE(ProcessusThematique.EAU, "fa-flask-vial"),
  FLOTTE_VEHICULES_EXPLOITES_ENTREPRISE(
      ProcessusThematique.LOGISTIQUE_TRANSPORTS, "fa-van-shuttle"),
  DISPONIBILITE_QUALITE_RESEAUX_TRANSPORT_MARCHANDISE(
      ProcessusThematique.LOGISTIQUE_TRANSPORTS, "fa-truck-fast"),
  DEPLACEMENTS_DOMICILE_TRAVAIL_PROFESSIONNELS(
      ProcessusThematique.LOGISTIQUE_TRANSPORTS, "fa-car-side"),
  PERTINENCE_OFFRE_MARCHE(ProcessusThematique.DEMANDE, "fa-store");

  private static final Map<ProcessusThematique, Set<ProcessusType>> THEMATIQUE_TYPES =
      Maps.immutableEnumMap(
          Arrays.stream(values())
              .collect(
                  Collectors.groupingBy(
                      ProcessusType::getThematique,
                      () -> new EnumMap<>(ProcessusThematique.class),
                      Collectors.collectingAndThen(
                          Collectors.toCollection(() -> EnumSet.noneOf(ProcessusType.class)),
                          Sets::immutableEnumSet))));

  private final ProcessusThematique thematique;
  private final String iconCssClass;

  ProcessusType(ProcessusThematique thematique, String iconCssClass) {
    this.thematique = thematique;
    this.iconCssClass = iconCssClass;
  }

  public ProcessusThematique getThematique() {
    return thematique;
  }

  public String getIconCssClass() {
    return iconCssClass;
  }

  public static Collection<ProcessusType> listByThematique(ProcessusThematique thematique) {
    return THEMATIQUE_TYPES.getOrDefault(thematique, ImmutableSet.of());
  }
}
