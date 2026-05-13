package sekoya.back.business.site.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import sekoya.back.business.site.model.Site;

public interface ISiteService extends IGenericEntityService<Long, Site> {

  void saveSite(Site site) throws ServiceException, SecurityServiceException;
}
