package sekoya.back.business.processus.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;

public interface IProcessusService extends IGenericEntityService<Long, Processus> {

  void saveProcessus(Processus processus) throws ServiceException, SecurityServiceException;

  void refreshRisqueBrut(Processus processus) throws ServiceException, SecurityServiceException;

  void enable(Processus processus) throws ServiceException, SecurityServiceException;

  void disable(Processus processus) throws ServiceException, SecurityServiceException;

  Risque getRisqueBrut(Processus processus, SimulationSearchDto simulationSearchDto);

  Processus getBySiteAndType(Site site, ProcessusType type);
}
