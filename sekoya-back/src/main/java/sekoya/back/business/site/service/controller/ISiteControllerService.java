package sekoya.back.business.site.service.controller;

import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.SITE_DISABLE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.SITE_ENABLE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.SITE_WRITE;

import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.site.model.Site;

public interface ISiteControllerService {

  @PreAuthorize(SITE_WRITE)
  void saveSite(@PermissionObject Site site) throws SecurityServiceException, ServiceException;

  @PreAuthorize(SITE_ENABLE)
  void enable(@PermissionObject Site site) throws ServiceException, SecurityServiceException;

  @PreAuthorize(SITE_DISABLE)
  void disable(@PermissionObject Site site) throws ServiceException, SecurityServiceException;

  Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom);
}
