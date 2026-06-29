package sekoya.back.business.alea.service;

import java.util.List;
import java.util.Objects;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.javatuples.Quartet;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.dao.IAleaDao;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.donneeclimatique.model.DonneeClimatique;
import sekoya.back.business.donneeclimatique.service.IDonneeClimatiqueService;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.util.binding.Bindings;

@Service
public class AleaServiceImpl extends GenericEntityServiceImpl<Long, Alea> implements IAleaService {

  private final IHistoryEventSummaryService historyEventSummaryService;

  private final IDonneeClimatiqueService donneeClimatiqueService;

  public AleaServiceImpl(
      IAleaDao dao,
      IDonneeClimatiqueService donneeClimatiqueService,
      IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.donneeClimatiqueService = donneeClimatiqueService;
    this.historyEventSummaryService = historyEventSummaryService;
  }

  @Override
  protected void createEntity(Alea entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getCreation());
    historyEventSummaryService.refresh(entity.getModification());
    super.createEntity(entity);
  }

  @Override
  protected void updateEntity(Alea entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getModification());
    super.updateEntity(entity);
  }

  @Override
  public void refreshRisqueBrut(Alea alea) throws ServiceException, SecurityServiceException {
    for (var data :
        List.of(
            Quartet.with(
                Bindings.alea().evolutionRcp45Annee2035(),
                Bindings.alea().risqueBrutRcp45Annee2035(),
                Scenario.RCP_4_5,
                Horizon.ANNEE_2035),
            Quartet.with(
                Bindings.alea().evolutionRcp45Annee2055(),
                Bindings.alea().risqueBrutRcp45Annee2055(),
                Scenario.RCP_4_5,
                Horizon.ANNEE_2055),
            Quartet.with(
                Bindings.alea().evolutionRcp85Annee2035(),
                Bindings.alea().risqueBrutRcp85Annee2035(),
                Scenario.RCP_8_5,
                Horizon.ANNEE_2035),
            Quartet.with(
                Bindings.alea().evolutionRcp85Annee2055(),
                Bindings.alea().risqueBrutRcp85Annee2055(),
                Scenario.RCP_8_5,
                Horizon.ANNEE_2055))) {
      Evolution evolution;

      if (Objects.equals(alea.getType(), AleaType.INONDATION_COTIERE)) {
        evolution =
            alea.getProcessus() != null
                    && alea.getProcessus().getSite() != null
                    && alea.getProcessus().getSite().getLittoral().isZoneSubmersible()
                ? Evolution.FORTEMENT_DEFAVORABLE
                : Evolution.PAS_EVOLUTION;
      } else {
        DonneeClimatique donneeClimatique =
            donneeClimatiqueService.getByAlea(alea, data.getValue2(), data.getValue3());
        evolution =
            donneeClimatique != null ? donneeClimatique.getEvolution() : Evolution.FAVORABLE;
      }

      data.getValue0().setWithRoot(alea, evolution);
      data.getValue1()
          .setWithRoot(
              alea,
              AleaRisqueBrutCalculator.generer(
                  alea.getImpactPotentielBrut(), data.getValue0().getSafelyWithRoot(alea)));
    }
  }

  @Override
  public Risque getRisqueBrut(Alea alea, SimulationSearchDto simulationSearchDto) {
    Objects.requireNonNull(alea);

    Evolution evolution;

    if (Objects.equals(alea.getType(), AleaType.INONDATION_COTIERE)) {
      evolution =
          alea.getProcessus().getSite().getLittoral().isZoneSubmersible()
              ? Evolution.FORTEMENT_DEFAVORABLE
              : Evolution.PAS_EVOLUTION;
    } else {
      DonneeClimatique donneeClimatique =
          donneeClimatiqueService.getByAlea(
              alea, simulationSearchDto.getScenario(), simulationSearchDto.getHorizon());
      evolution = donneeClimatique != null ? donneeClimatique.getEvolution() : Evolution.FAVORABLE;
    }

    return AleaRisqueBrutCalculator.generer(alea.getImpactPotentielBrut(), evolution);
  }
}
