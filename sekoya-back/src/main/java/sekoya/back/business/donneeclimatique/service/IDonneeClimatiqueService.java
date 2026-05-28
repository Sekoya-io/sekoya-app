package sekoya.back.business.donneeclimatique.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.donneeclimatique.model.DonneeClimatique;
import sekoya.back.business.donneeclimatique.model.PointGeographique;

public interface IDonneeClimatiqueService extends IGenericEntityService<Long, DonneeClimatique> {

  DonneeClimatique getByAlea(Alea alea, Scenario scenario, Horizon horizon);

  DonneeClimatique getPlusDefavorableByPointGeographique(
      PointGeographique pointGeographique, Scenario scenario, Horizon horizon);
}
