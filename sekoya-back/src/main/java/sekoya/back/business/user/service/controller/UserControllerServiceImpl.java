package sekoya.back.business.user.service.controller;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sekoya.back.business.common.model.EmailAddress;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.service.business.IUserService;

@Service
public class UserControllerServiceImpl implements IUserControllerService {

  private final IUserService userService;

  @Autowired
  public UserControllerServiceImpl(IUserService userService) {
    this.userService = userService;
  }

  @Override
  public void saveUserOrganisation(User user, String password)
      throws SecurityServiceException, ServiceException {
    userService.saveUserOrganisation(user, password);
  }

  @Override
  public void saveUserAdministrateurFonctionnel(User user, String password)
      throws SecurityServiceException, ServiceException {
    userService.saveUserAdministrateurFonctionnel(user, password);
  }

  @Override
  public void saveUserAdministrateurTechnique(User user, String password)
      throws SecurityServiceException, ServiceException {
    userService.saveUserAdministrateurTechnique(user, password);
  }

  @Override
  public void updateRoles(User user) throws SecurityServiceException, ServiceException {
    userService.updateRoles(user);
  }

  @Override
  public void enable(User user) throws SecurityServiceException, ServiceException {
    userService.enable(user);
  }

  @Override
  public void disable(User user) throws SecurityServiceException, ServiceException {
    userService.disable(user);
  }

  @Override
  public void openAnnouncement(User user) throws ServiceException, SecurityServiceException {
    userService.openAnnouncement(user);
  }

  @Override
  public void closeAnnouncement(User user) throws ServiceException, SecurityServiceException {
    userService.closeAnnouncement(user);
  }

  @Override
  public void initPasswordRecoveryRequest(EmailAddress emailAddress)
      throws SecurityServiceException, ServiceException {
    userService.initPasswordRecoveryRequest(emailAddress);
  }

  @Override
  public void onSignIn(User user) throws ServiceException, SecurityServiceException {
    userService.onSignIn(user);
  }

  @Override
  public void onSignInFail(User user) throws ServiceException, SecurityServiceException {
    userService.onSignInFail(user);
  }

  @Override
  public User getByEmailAddressCaseInsensitive(EmailAddress emailAddress) {
    return userService.getByEmailAddressCaseInsensitive(emailAddress);
  }

  @Override
  public User getByUsername(String value) {
    return userService.getByUsername(value);
  }
}
