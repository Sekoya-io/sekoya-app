package sekoya.back.business.organisation.service.controller;

import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.ORGANISATION_WRITE;

import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import sekoya.back.business.organisation.model.Organisation;

public interface IOrganisationControllerService {

  @PreAuthorize(ORGANISATION_WRITE)
  void saveOrganisation(@PermissionObject Organisation organisation)
      throws SecurityServiceException, ServiceException;

  Organisation getByNomCaseInsensitive(String nom);
}
