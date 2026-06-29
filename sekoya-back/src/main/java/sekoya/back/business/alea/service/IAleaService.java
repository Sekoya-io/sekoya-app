package sekoya.back.business.alea.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.simulation.dto.SimulationSearchDto;

public interface IAleaService extends IGenericEntityService<Long, Alea> {

  void refreshRisqueBrut(Alea alea) throws ServiceException, SecurityServiceException;

  Risque getRisqueBrut(Alea alea, SimulationSearchDto simulationSearchDto);
}
