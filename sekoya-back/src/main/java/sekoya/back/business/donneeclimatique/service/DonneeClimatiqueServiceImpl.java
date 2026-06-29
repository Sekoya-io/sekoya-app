package sekoya.back.business.donneeclimatique.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.donneeclimatique.dao.IDonneeClimatiqueDao;
import sekoya.back.business.donneeclimatique.model.DonneeClimatique;
import sekoya.back.business.donneeclimatique.model.PointGeographique;
import sekoya.back.business.history.service.IHistoryEventSummaryService;

@Service
public class DonneeClimatiqueServiceImpl extends GenericEntityServiceImpl<Long, DonneeClimatique>
    implements IDonneeClimatiqueService {

  private final IDonneeClimatiqueDao dao;

  public DonneeClimatiqueServiceImpl(
      IDonneeClimatiqueDao dao, IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public DonneeClimatique getByAlea(Alea alea, Scenario scenario, Horizon horizon) {
    if (alea == null || scenario == null || horizon == null) {
      return null;
    }
    return dao.getByAlea(alea, scenario, horizon);
  }

  @Override
  public DonneeClimatique getPlusDefavorableByPointGeographique(
      PointGeographique pointGeographique, Scenario scenario, Horizon horizon) {
    if (pointGeographique == null || scenario == null || horizon == null) {
      return null;
    }
    return dao.getPlusDefavorableByPointGeographique(pointGeographique, scenario, horizon);
  }

  @Override
  public Risque getRisqueByAleaTypeAndPointGeographique(
      AleaType aleaType, PointGeographique pointGeographique, Scenario scenario, Horizon horizon) {
    return dao.getRisqueByAleaTypeAndPointGeographique(
        aleaType, pointGeographique, scenario, horizon);
  }
}
