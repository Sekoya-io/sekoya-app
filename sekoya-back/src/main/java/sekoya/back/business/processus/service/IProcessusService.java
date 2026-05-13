package sekoya.back.business.processus.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import sekoya.back.business.processus.model.Processus;

public interface IProcessusService extends IGenericEntityService<Long, Processus> {

  void saveProcessus(Processus processus) throws ServiceException, SecurityServiceException;
}
