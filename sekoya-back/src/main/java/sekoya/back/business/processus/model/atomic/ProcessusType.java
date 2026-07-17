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
  FOURNISSEUR_1(ProcessusThematique.APPROVISIONNEMENTS),
  FOURNISSEUR_2(ProcessusThematique.APPROVISIONNEMENTS),
  FOURNISSEUR_3(ProcessusThematique.APPROVISIONNEMENTS),
  FOURNISSEUR_4(ProcessusThematique.APPROVISIONNEMENTS),
  INTEGRITE_BATIMENTS(ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS),
  INTEGRITE_ENTREPOTS_STOCKS(ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS),
  PRATICABILITE_VOIRIE_INTERNE_ZONES_EXTERIEURES(ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS),
  INTEGRITE_FONCTIONNEMENT_EQUIPEMENTS_PRODUCTION(ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS),
  INTEGRITE_FONCTIONNEMENT_EQUIPEMENTS_ALIMENTATION_ELECTRIQUE(
      ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS),
  RESEAUX_CENTRES_DONNEES_SITE(ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS),
  INTEGRITE_FONCTIONNEMENT_EQUIPEMENTS_PRODUCTION_FROID(
      ProcessusThematique.INFRASTRUCTURE_EQUIPEMENTS),
  MAINTIEN_CONDITIONS_PRODUCTION_INTERIEUR(ProcessusThematique.OPERATIONS_PROCESSUS),
  MAINTIEN_CONDITIONS_PRODUCTION_EXTERIEUR(ProcessusThematique.OPERATIONS_PROCESSUS),
  MAINTIEN_CONDITIONS_TRAVAIL_INTERIEUR(ProcessusThematique.OPERATIONS_PROCESSUS),
  MAINTIEN_CONDITIONS_TRAVAIL_EXTERIEUR(ProcessusThematique.OPERATIONS_PROCESSUS),
  EXPLOITATION_RESSOURCES_NATURELLES_AGRICOLES_HALIEUTIQUES(
      ProcessusThematique.OPERATIONS_PROCESSUS),
  EXPLOITATION_RESSOURCES_NATURELLES_TOURISTIQUES(ProcessusThematique.OPERATIONS_PROCESSUS),
  STOCKAGE_EVACUATION_EAUX_USEES(ProcessusThematique.OPERATIONS_PROCESSUS),
  STOCKAGE_EVACUATION_DECHETS(ProcessusThematique.OPERATIONS_PROCESSUS),
  ELECTRICITE(ProcessusThematique.ENERGIE_TELECOMS),
  GAZ(ProcessusThematique.ENERGIE_TELECOMS),
  RESEAU_CHALEUR_FROID(ProcessusThematique.ENERGIE_TELECOMS),
  APPROVISIONNEMENT_STOCK_COMBUSTIBLE_SITE(ProcessusThematique.ENERGIE_TELECOMS),
  DISPONIBILITE_QUALITE_RESEAUX_TELECOMS_INTERNET(ProcessusThematique.ENERGIE_TELECOMS),
  APPROVISIONNEMENT_EAU_PRINCIPAL_QUANTITE(ProcessusThematique.EAU),
  APPROVISIONNEMENT_EAU_PRINCIPAL_QUALITE(ProcessusThematique.EAU),
  FLOTTE_VEHICULES_EXPLOITES_ENTREPRISE(ProcessusThematique.LOGISTIQUE_TRANSPORTS),
  DISPONIBILITE_QUALITE_RESEAUX_TRANSPORT_MARCHANDISE(ProcessusThematique.LOGISTIQUE_TRANSPORTS),
  DEPLACEMENTS_DOMICILE_TRAVAIL_PROFESSIONNELS(ProcessusThematique.LOGISTIQUE_TRANSPORTS),
  PERTINENCE_OFFRE_MARCHE(ProcessusThematique.DEMANDE);

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

  ProcessusType(ProcessusThematique thematique) {
    this.thematique = thematique;
  }

  public ProcessusThematique getThematique() {
    return thematique;
  }

  public static Collection<ProcessusType> listByThematique(ProcessusThematique thematique) {
    return THEMATIQUE_TYPES.getOrDefault(thematique, ImmutableSet.of());
  }
}
