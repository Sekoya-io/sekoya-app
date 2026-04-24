package sekoya.back.security.service.permission;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ROLE_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ROLE_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.ROLE_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.ROLE_WRITE;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.user.model.User;

@Service
public class RolePermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<Role>
    implements IRolePermissionEvaluator {

  @Override
  public boolean hasPermission(User user, Role role, Permission permission) {
    if (is(permission, ROLE_READ)) {
      return canRead(user);
    } else if (is(permission, ROLE_WRITE)) {
      return canWrite(user);
    }
    return false;
  }

  @VisibleForTesting
  public boolean canRead(User user) {
    return hasPermission(user, GLOBAL_ROLE_READ);
  }

  @VisibleForTesting
  public boolean canWrite(User user) {
    return canRead(user) && hasPermission(user, GLOBAL_ROLE_WRITE);
  }
}
