package sekoya.back.security.service.permission;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;
import sekoya.back.business.user.model.User;

public interface IUserPermissionEvaluator extends IGenericPermissionEvaluator<User, User> {}
