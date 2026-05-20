package sekoya.back.business.processus.service.controller;

import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.PROCESSUS_DISABLE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.PROCESSUS_ENABLE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.PROCESSUS_WRITE;

import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.site.model.Site;

public interface IProcessusControllerService {

  @PreAuthorize(PROCESSUS_WRITE)
  void saveProcessus(@PermissionObject Processus processus)
      throws SecurityServiceException, ServiceException;

  @PreAuthorize(PROCESSUS_ENABLE)
  void enable(@PermissionObject Processus processus)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(PROCESSUS_DISABLE)
  void disable(@PermissionObject Processus processus)
      throws ServiceException, SecurityServiceException;

  Processus getBySiteAndType(Site site, ProcessusType type);
}
