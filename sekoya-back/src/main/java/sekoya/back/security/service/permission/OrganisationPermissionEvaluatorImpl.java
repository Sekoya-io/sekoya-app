package sekoya.back.security.service.permission;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ORGANISATION_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.ORGANISATION_WRITE;

import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.user.model.User;

@Service
public class OrganisationPermissionEvaluatorImpl
    extends AbstractGenericPermissionEvaluator<Organisation>
    implements IOrganisationPermissionEvaluator {

  @Override
  public boolean hasPermission(User user, Organisation announcement, Permission permission) {
    if (is(permission, ORGANISATION_WRITE)) {
      return canWriteOrganisation(user);
    }
    return false;
  }

  public boolean canWriteOrganisation(User user) {
    return hasPermission(user, GLOBAL_ORGANISATION_WRITE);
  }
}
