package sekoya.back.security.service;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import sekoya.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.security.model.SecurityOptions;

public interface ISecurityManagementService extends ITransactionalAspectAwareService {

  void updatePassword(User user, String password, User author)
      throws ServiceException, SecurityServiceException;

  void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException;

  void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws ServiceException, SecurityServiceException;

  boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException;

  boolean isPasswordExpired(User user);

  boolean isPasswordRecoveryRequestExpired(User user);

  SecurityOptions getSecurityOptionsDefault();

  SecurityOptions getSecurityOptions(UserType userType);

  SecurityOptions getSecurityOptions(User user);
}
