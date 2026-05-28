package sekoya.back.business.donneeclimatique.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.donneeclimatique.model.DonneeClimatique;
import sekoya.back.business.donneeclimatique.model.PointGeographique;

public interface IDonneeClimatiqueDao extends IGenericEntityDao<Long, DonneeClimatique> {

  DonneeClimatique getByAlea(Alea alea, Scenario scenario, Horizon horizon);

  DonneeClimatique getPlusDefavorableByPointGeographique(
      PointGeographique pointGeographique, Scenario scenario, Horizon horizon);
}
