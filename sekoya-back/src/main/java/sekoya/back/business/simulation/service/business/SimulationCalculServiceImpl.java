package sekoya.back.business.simulation.service.business;

import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import org.apache.commons.compress.utils.Lists;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.alea.service.AleaRisqueBrutCalculator;
import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.donneeclimatique.model.DonneeClimatique;
import sekoya.back.business.donneeclimatique.service.IDonneeClimatiqueService;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.predicate.ProcessusPredicates;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;

@Service
public class SimulationCalculServiceImpl implements ISimulationCalculService {

  private final IDonneeClimatiqueService donneeClimatiqueService;

  public SimulationCalculServiceImpl(IDonneeClimatiqueService donneeClimatiqueService) {
    super();
    this.donneeClimatiqueService = donneeClimatiqueService;
  }

  @Override
  public Risque getSiteRisqueBrut(Site site, SimulationSearchDto simulationSearchDto) {
    Objects.requireNonNull(site);

    List<Processus> processus =
        site.getProcessus().stream().filter(ProcessusPredicates.enabled()).toList();

    if (simulationSearchDto.isApplyProcessus()) {
      if (!processus.isEmpty()) {
        List<Risque> processusRisques = Lists.newArrayList();
        for (Processus p : processus) {
          processusRisques.add(getProcessusRisqueBrut(p, simulationSearchDto));
        }
        return processusRisques.stream()
            .max(Comparator.comparingInt(Risque::getScore))
            .orElseThrow();
      } else {
        return Risque.OPPORTUNITE;
      }
    } else {
      Risque risqueInondationCotiere =
          site.getLittoral().isZoneSubmersible()
              ? Evolution.FORTEMENT_DEFAVORABLE.getRisque()
              : Evolution.PAS_EVOLUTION.getRisque();

      if (site.getLittoral().isZoneSubmersible()) {
        return risqueInondationCotiere;
      }

      Risque risquePlusDefavorableByPointGeographique =
          donneeClimatiqueService
              .getPlusDefavorableByPointGeographique(
                  site.getPointGeographique(),
                  simulationSearchDto.getScenario(),
                  simulationSearchDto.getHorizon())
              .getEvolution()
              .getRisque();
      return Risque.fromScore(
          Math.max(
              risqueInondationCotiere.getScore(),
              risquePlusDefavorableByPointGeographique.getScore()));
    }
  }

  @Override
  public Risque getProcessusRisqueBrut(
      Processus processus, SimulationSearchDto simulationSearchDto) {
    Objects.requireNonNull(processus);

    if (processus.getAleas().isEmpty()) {
      Risque risqueInondationCotiere =
          processus.getSite().getLittoral().isZoneSubmersible()
              ? Evolution.FORTEMENT_DEFAVORABLE.getRisque()
              : Evolution.PAS_EVOLUTION.getRisque();

      if (processus.getSite().getLittoral().isZoneSubmersible()) {
        return risqueInondationCotiere;
      }

      Risque risquePlusDefavorableByPointGeographique =
          donneeClimatiqueService
              .getPlusDefavorableByPointGeographique(
                  processus.getSite().getPointGeographique(),
                  simulationSearchDto.getScenario(),
                  simulationSearchDto.getHorizon())
              .getEvolution()
              .getRisque();
      return Risque.fromScore(
          Math.max(
              risqueInondationCotiere.getScore(),
              risquePlusDefavorableByPointGeographique.getScore()));
    } else {
      List<Risque> aleasRisques = Lists.newArrayList();
      for (Alea alea : processus.getAleas()) {
        aleasRisques.add(getAleaRisqueBrut(alea, simulationSearchDto));
      }
      return aleasRisques.stream().max(Comparator.comparingInt(Risque::getScore)).orElseThrow();
    }
  }

  @Override
  public Evolution getAleaEvolution(Alea alea, SimulationSearchDto simulationSearchDto) {
    Objects.requireNonNull(alea);

    if (Objects.equals(alea.getType(), AleaType.INONDATION_COTIERE)) {
      return alea.getProcessus().getSite().getLittoral().isZoneSubmersible()
          ? Evolution.FORTEMENT_DEFAVORABLE
          : Evolution.PAS_EVOLUTION;
    } else {
      DonneeClimatique donneeClimatique =
          donneeClimatiqueService.getByAlea(
              alea, simulationSearchDto.getScenario(), simulationSearchDto.getHorizon());
      return donneeClimatique != null ? donneeClimatique.getEvolution() : Evolution.FAVORABLE;
    }
  }

  @Override
  public Risque getAleaRisqueBrut(Alea alea, SimulationSearchDto simulationSearchDto) {
    Objects.requireNonNull(alea);

    return AleaRisqueBrutCalculator.generer(
        alea.getImpactPotentielBrut(), getAleaEvolution(alea, simulationSearchDto));
  }

  @Override
  public SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographiqueBySite(
      Site site, SimulationSearchDto simulationSearchDto) {
    SortedSet<Pair<AleaType, Risque>> aleasRisques =
        Sets.newTreeSet(
            Comparator.comparing((Pair<AleaType, Risque> p) -> p.getValue1().getScore())
                .reversed()
                .thenComparing(p -> p.getValue0().name()));

    for (AleaType aleaType : AleaType.values()) {
      if (Objects.equals(aleaType, AleaType.INONDATION_COTIERE)) {
        aleasRisques.add(
            Pair.with(
                AleaType.INONDATION_COTIERE,
                site.getLittoral().isZoneSubmersible()
                    ? Evolution.FORTEMENT_DEFAVORABLE.getRisque()
                    : Evolution.PAS_EVOLUTION.getRisque()));
      } else {
        aleasRisques.add(
            Pair.with(
                aleaType,
                donneeClimatiqueService.getRisqueByAleaTypeAndPointGeographique(
                    aleaType,
                    site.getPointGeographique(),
                    simulationSearchDto.getScenario(),
                    simulationSearchDto.getHorizon())));
      }
    }

    return aleasRisques;
  }
}
