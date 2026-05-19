package sekoya.back.business.organisation.service.controller;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.organisation.service.business.IOrganisationService;

@Service
public class OrganisationControllerServiceImpl implements IOrganisationControllerService {

  private final IOrganisationService organisationService;

  public OrganisationControllerServiceImpl(IOrganisationService organisationService) {
    this.organisationService = organisationService;
  }

  @Override
  public void saveOrganisation(Organisation organisation)
      throws SecurityServiceException, ServiceException {
    organisationService.saveOrganisation(organisation);
  }

  @Override
  public Organisation getByNomCaseInsensitive(String nom) {
    return organisationService.getByNomCaseInsensitive(nom);
  }
}
