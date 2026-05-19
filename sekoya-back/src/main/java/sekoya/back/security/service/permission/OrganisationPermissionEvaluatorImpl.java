package sekoya.back.security.service.permission;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ORGANISATION_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.ORGANISATION_WRITE;

import java.util.Objects;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.predicate.UserPredicates;

@Service
public class OrganisationPermissionEvaluatorImpl
    extends AbstractGenericPermissionEvaluator<Organisation>
    implements IOrganisationPermissionEvaluator {

  @Override
  public boolean hasPermission(User user, Organisation organisation, Permission permission) {
    if (is(permission, ORGANISATION_WRITE)) {
      return canWriteOrganisation(user, organisation);
    }
    return false;
  }

  @Override
  public boolean isVisible(User user, Organisation organisation) {
    return user != null
        && organisation != null
        && (UserPredicates.administrateur().apply(user)
            || (user.getUserOrganisation() != null
                && Objects.equals(user.getUserOrganisation().getOrganisation(), organisation)));
  }

  public boolean canWriteOrganisation(User user, Organisation organisation) {
    return hasPermission(user, GLOBAL_ORGANISATION_WRITE);
  }
}
