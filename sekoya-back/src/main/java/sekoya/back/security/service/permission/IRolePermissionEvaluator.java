package sekoya.back.security.service.permission;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.user.model.User;

public interface IRolePermissionEvaluator extends IGenericPermissionEvaluator<User, Role> {}
