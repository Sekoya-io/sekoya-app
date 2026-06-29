package sekoya.back.business.processus.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.compress.utils.Lists;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.service.IAleaService;
import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.donneeclimatique.service.IDonneeClimatiqueService;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.processus.dao.IProcessusDao;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.service.business.ISiteService;
import sekoya.back.util.binding.Bindings;

@Service
public class ProcessusServiceImpl extends GenericEntityServiceImpl<Long, Processus>
    implements IProcessusService {

  private final IProcessusDao dao;
  private final ISiteService siteService;
  private final IAleaService aleaService;
  private final IDonneeClimatiqueService donneeClimatiqueService;
  private final IHistoryEventSummaryService historyEventSummaryService;

  public ProcessusServiceImpl(
      IProcessusDao dao,
      ISiteService siteService,
      IAleaService aleaService,
      IDonneeClimatiqueService donneeClimatiqueService,
      IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.dao = dao;
    this.siteService = siteService;
    this.aleaService = aleaService;
    this.donneeClimatiqueService = donneeClimatiqueService;
    this.historyEventSummaryService = historyEventSummaryService;
  }

  @Override
  protected void createEntity(Processus entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getCreation());
    historyEventSummaryService.refresh(entity.getModification());
    super.createEntity(entity);
  }

  @Override
  protected void updateEntity(Processus entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getModification());
    super.updateEntity(entity);
  }

  @Override
  public void saveProcessus(Processus processus) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(processus);

    if (processus.isNew()) {
      processus.getSite().addProcessus(processus);
    }

    siteService.refreshRisqueBrut(processus.getSite(), processus);

    if (processus.isNew()) {
      create(processus);
    } else {
      update(processus);
    }

    for (Alea alea : processus.getAleas()) {
      if (alea.isNew()) {
        aleaService.create(alea);
      } else {
        aleaService.update(alea);
      }
    }

    siteService.update(processus.getSite());
  }

  @Override
  public void refreshRisqueBrut(Processus processus)
      throws ServiceException, SecurityServiceException {
    for (Alea alea : processus.getAleas()) {
      aleaService.refreshRisqueBrut(alea);
    }

    if (processus.getAleas().isEmpty()) {
      for (var data :
          List.of(
              Triplet.with(
                  Bindings.processus().risqueBrutRcp45Annee2035(),
                  Scenario.RCP_4_5,
                  Horizon.ANNEE_2035),
              Triplet.with(
                  Bindings.processus().risqueBrutRcp45Annee2055(),
                  Scenario.RCP_4_5,
                  Horizon.ANNEE_2055),
              Triplet.with(
                  Bindings.processus().risqueBrutRcp85Annee2035(),
                  Scenario.RCP_8_5,
                  Horizon.ANNEE_2035),
              Triplet.with(
                  Bindings.processus().risqueBrutRcp85Annee2055(),
                  Scenario.RCP_8_5,
                  Horizon.ANNEE_2055))) {
        data.getValue0()
            .setWithRoot(
                processus,
                donneeClimatiqueService
                    .getPlusDefavorableByPointGeographique(
                        processus.getSite().getPointGeographique(),
                        data.getValue1(),
                        data.getValue2())
                    .getEvolution()
                    .getRisque());
      }
    } else {
      for (var data :
          List.of(
              Pair.with(
                  Bindings.processus().risqueBrutRcp45Annee2035(),
                  Bindings.alea().risqueBrutRcp45Annee2035()),
              Pair.with(
                  Bindings.processus().risqueBrutRcp45Annee2055(),
                  Bindings.alea().risqueBrutRcp45Annee2055()),
              Pair.with(
                  Bindings.processus().risqueBrutRcp85Annee2035(),
                  Bindings.alea().risqueBrutRcp85Annee2035()),
              Pair.with(
                  Bindings.processus().risqueBrutRcp85Annee2055(),
                  Bindings.alea().risqueBrutRcp85Annee2055()))) {
        data.getValue0()
            .setWithRoot(
                processus,
                processus.getAleas().stream()
                    .map(data.getValue1())
                    .filter(Objects::nonNull)
                    .max(Comparator.comparingInt(Risque::getScore))
                    .orElseThrow());
      }
    }
  }

  @Override
  public Risque getRisqueBrut(Processus processus, SimulationSearchDto simulationSearchDto) {
    Objects.requireNonNull(processus);

    if (processus.getAleas().isEmpty()) {
      // TODO : à vérifier, pas fait dans les méthodes dénormalisées
      Risque risqueInondationCotiere =
          processus.getSite().getLittoral().isZoneSubmersible()
              ? Evolution.FORTEMENT_DEFAVORABLE.getRisque()
              : Evolution.PAS_EVOLUTION.getRisque();

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
        aleasRisques.add(aleaService.getRisqueBrut(alea, simulationSearchDto));
      }
      return aleasRisques.stream().max(Comparator.comparingInt(Risque::getScore)).orElseThrow();
    }
  }

  @Override
  public void enable(Processus processus) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(processus);
    processus.setEnabled(true);
    siteService.refreshRisqueBrut(processus.getSite());
    update(processus);
  }

  @Override
  public void disable(Processus processus) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(processus);
    processus.setEnabled(false);
    siteService.refreshRisqueBrut(processus.getSite());
    update(processus);
  }

  @Override
  public Processus getBySiteAndType(Site site, ProcessusType type) {
    if (site == null || type == null) {
      return null;
    }
    return dao.getBySiteAndType(site, type);
  }
}
