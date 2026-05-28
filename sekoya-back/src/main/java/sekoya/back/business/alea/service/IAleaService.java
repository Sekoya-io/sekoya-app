package sekoya.back.business.alea.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import sekoya.back.business.alea.model.Alea;

public interface IAleaService extends IGenericEntityService<Long, Alea> {

  void refreshRisqueBrut(Alea alea) throws ServiceException, SecurityServiceException;
}
