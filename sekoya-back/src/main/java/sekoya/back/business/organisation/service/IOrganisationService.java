package sekoya.back.business.organisation.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import sekoya.back.business.organisation.model.Organisation;

public interface IOrganisationService extends IGenericEntityService<Long, Organisation> {

  void saveOrganisation(Organisation organisation)
      throws ServiceException, SecurityServiceException;
}
