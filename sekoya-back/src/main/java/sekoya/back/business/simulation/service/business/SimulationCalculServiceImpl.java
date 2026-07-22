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
import sekoya.back.business.simulation.dto.SimulationParametresDto;
import sekoya.back.business.site.model.Site;

@Service
public class SimulationCalculServiceImpl implements ISimulationCalculService {

  private final IDonneeClimatiqueService donneeClimatiqueService;

  public SimulationCalculServiceImpl(IDonneeClimatiqueService donneeClimatiqueService) {
    super();
    this.donneeClimatiqueService = donneeClimatiqueService;
  }

  @Override
  public Risque getSiteRisqueBrut(Site site, SimulationParametresDto simulationParametresDto) {
    Objects.requireNonNull(site);

    SortedSet<Processus> processus = site.getProcessusEnabled();

    if (simulationParametresDto.isEnableProcessus()) {
      if (!processus.isEmpty()) {
        List<Risque> processusRisques = Lists.newArrayList();
        for (Processus p : processus) {
          processusRisques.add(getProcessusRisqueBrut(p, simulationParametresDto));
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
                  simulationParametresDto.getScenario(),
                  simulationParametresDto.getHorizon())
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
      Processus processus, SimulationParametresDto simulationParametresDto) {
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
                  simulationParametresDto.getScenario(),
                  simulationParametresDto.getHorizon())
              .getEvolution()
              .getRisque();
      return Risque.fromScore(
          Math.max(
              risqueInondationCotiere.getScore(),
              risquePlusDefavorableByPointGeographique.getScore()));
    } else {
      List<Risque> aleasRisques = Lists.newArrayList();
      for (Alea alea : processus.getAleas()) {
        aleasRisques.add(getAleaRisqueBrut(alea, simulationParametresDto));
      }
      return aleasRisques.stream().max(Comparator.comparingInt(Risque::getScore)).orElseThrow();
    }
  }

  @Override
  public Evolution getAleaEvolution(Alea alea, SimulationParametresDto simulationParametresDto) {
    Objects.requireNonNull(alea);

    if (Objects.equals(alea.getType(), AleaType.INONDATION_COTIERE)) {
      return alea.getProcessus().getSite().getLittoral().isZoneSubmersible()
          ? Evolution.FORTEMENT_DEFAVORABLE
          : Evolution.PAS_EVOLUTION;
    } else {
      DonneeClimatique donneeClimatique =
          donneeClimatiqueService.getByAlea(
              alea, simulationParametresDto.getScenario(), simulationParametresDto.getHorizon());
      return donneeClimatique != null ? donneeClimatique.getEvolution() : Evolution.FAVORABLE;
    }
  }

  @Override
  public Risque getAleaRisqueBrut(Alea alea, SimulationParametresDto simulationParametresDto) {
    Objects.requireNonNull(alea);

    return AleaRisqueBrutCalculator.generer(
        alea.getImpactPotentielBrut(), getAleaEvolution(alea, simulationParametresDto));
  }

  @Override
  public SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographiqueBySite(
      Site site, SimulationParametresDto simulationParametresDto) {
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
                    simulationParametresDto.getScenario(),
                    simulationParametresDto.getHorizon())));
      }
    }

    return aleasRisques;
  }
}
