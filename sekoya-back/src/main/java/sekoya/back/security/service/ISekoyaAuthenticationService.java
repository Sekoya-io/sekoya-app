package sekoya.back.security.service;

import org.iglooproject.jpa.security.service.IAuthenticationService;
import sekoya.back.business.user.model.User;

public interface ISekoyaAuthenticationService extends IAuthenticationService {

  User getUser();
}
