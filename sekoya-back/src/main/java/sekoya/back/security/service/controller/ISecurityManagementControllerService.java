package sekoya.back.security.service.controller;

import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import sekoya.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.security.model.SecurityOptions;
import sekoya.back.security.model.SekoyaSecurityExpressionConstants;

public interface ISecurityManagementControllerService {

  @PreAuthorize(SekoyaSecurityExpressionConstants.USER_EDIT_PASSWORD)
  void updatePassword(@PermissionObject User user, String password)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(SekoyaSecurityExpressionConstants.USER_RECOVERY_PASSWORD)
  void initiatePasswordRecoveryRequest(
      @PermissionObject User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(SekoyaSecurityExpressionConstants.ADMIN_EDIT_PASSWORD)
  void updatePassword(@PermissionObject User user, String password, User author)
      throws ServiceException, SecurityServiceException;

  @PreAuthorize(SekoyaSecurityExpressionConstants.ADMIN_RECOVERY_PASSWORD)
  void initiatePasswordRecoveryRequest(
      @PermissionObject User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws ServiceException, SecurityServiceException;

  boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException;

  boolean isPasswordExpired(User user);

  boolean isPasswordRecoveryRequestExpired(User user);

  SecurityOptions getSecurityOptions(UserType userType);

  SecurityOptions getSecurityOptions(User user);
}
