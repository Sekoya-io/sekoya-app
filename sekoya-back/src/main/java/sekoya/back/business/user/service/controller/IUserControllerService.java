package sekoya.back.business.user.service.controller;

import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.USER_ADMINISTRATEUR_FONCTIONNEL_WRITE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.USER_ADMINISTRATEUR_TECHNIQUE_WRITE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.USER_CLOSE_ANNONCEMENT;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.USER_DISABLE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.USER_ENABLE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.USER_OPEN_ANNONCEMENT;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.USER_ORGANISATION_WRITE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.USER_WRITE;

import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import sekoya.back.business.common.model.EmailAddress;
import sekoya.back.business.user.model.User;

public interface IUserControllerService {

  @PreAuthorize(USER_ORGANISATION_WRITE)
  void saveUserOrganisation(@PermissionObject User user, String password)
      throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_ADMINISTRATEUR_FONCTIONNEL_WRITE)
  void saveUserAdministrateurFonctionnel(@PermissionObject User user, String password)
      throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_ADMINISTRATEUR_TECHNIQUE_WRITE)
  void saveUserAdministrateurTechnique(@PermissionObject User user, String password)
      throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_WRITE)
  void updateRoles(@PermissionObject User user) throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_ENABLE)
  void enable(@PermissionObject User user) throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_DISABLE)
  void disable(@PermissionObject User user) throws SecurityServiceException, ServiceException;

  @PreAuthorize(USER_OPEN_ANNONCEMENT)
  void openAnnouncement(@PermissionObject User user)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(USER_CLOSE_ANNONCEMENT)
  void closeAnnouncement(@PermissionObject User user)
      throws ServiceException, SecurityServiceException;

  void initPasswordRecoveryRequest(EmailAddress emailAddress)
      throws SecurityServiceException, ServiceException;

  void onSignIn(User user) throws ServiceException, SecurityServiceException;

  void onSignInFail(User user) throws ServiceException, SecurityServiceException;

  User getByEmailAddressCaseInsensitive(EmailAddress emailAddress);

  User getByUsername(String value);
}
