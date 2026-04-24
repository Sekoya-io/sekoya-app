package sekoya.back.business.role.service.controller;

import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.ROLE_WRITE;

import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import sekoya.back.business.role.model.Role;

public interface IRoleControllerService {

  @PreAuthorize(ROLE_WRITE)
  void saveRole(@PermissionObject Role role) throws ServiceException, SecurityServiceException;

  Role getByTitle(String title);
}
