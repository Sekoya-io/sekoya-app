package sekoya.back.security.service;

import org.iglooproject.jpa.security.service.CoreAuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.service.business.IUserService;

public class SekoyaAuthenticationServiceImpl extends CoreAuthenticationServiceImpl
    implements ISekoyaAuthenticationService {

  @Autowired private IUserService userService;

  @Override
  public User getUser() {
    return userService.getAuthenticatedUser();
  }
}
