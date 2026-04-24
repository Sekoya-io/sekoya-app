package sekoya.back.security.service.controller;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import sekoya.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.security.model.SecurityOptions;
import sekoya.back.security.service.ISecurityManagementService;

@Service
public class SecurityManagerControllerService implements ISecurityManagementControllerService {

  private final ISecurityManagementService securityManagementService;

  @Autowired
  public SecurityManagerControllerService(ISecurityManagementService securityManagementService) {
    this.securityManagementService = securityManagementService;
  }

  @Override
  public void updatePassword(User user, String password)
      throws ServiceException, SecurityServiceException {
    securityManagementService.updatePassword(user, password, user);
  }

  @Override
  public void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException {
    securityManagementService.initiatePasswordRecoveryRequest(user, type, initiator, user);
  }

  @Override
  public void updatePassword(User user, String password, User author)
      throws ServiceException, SecurityServiceException {
    securityManagementService.updatePassword(user, password, author);
  }

  @Override
  public void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws ServiceException, SecurityServiceException {
    securityManagementService.initiatePasswordRecoveryRequest(user, type, initiator, author);
  }

  @Override
  public boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException {
    return securityManagementService.checkPassword(password, user);
  }

  @Override
  public boolean isPasswordExpired(User user) {
    return securityManagementService.isPasswordExpired(user);
  }

  @Override
  public boolean isPasswordRecoveryRequestExpired(User user) {
    return securityManagementService.isPasswordRecoveryRequestExpired(user);
  }

  @Override
  public SecurityOptions getSecurityOptions(UserType userType) {
    return securityManagementService.getSecurityOptions(userType);
  }

  @Override
  public SecurityOptions getSecurityOptions(User user) {
    return securityManagementService.getSecurityOptions(user);
  }
}
