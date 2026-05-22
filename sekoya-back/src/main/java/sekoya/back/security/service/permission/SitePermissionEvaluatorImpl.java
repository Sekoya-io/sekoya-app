package sekoya.back.security.service.permission;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_SITE_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_SITE_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_DISABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_ENABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_WRITE;

import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.user.model.User;

@Service
public class SitePermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<Site>
    implements ISitePermissionEvaluator {

  private final IOrganisationPermissionEvaluator organisationPermissionEvaluator;

  public SitePermissionEvaluatorImpl(
      IOrganisationPermissionEvaluator organisationPermissionEvaluator) {
    super();
    this.organisationPermissionEvaluator = organisationPermissionEvaluator;
  }

  @Override
  public boolean hasPermission(User user, Site site, Permission permission) {
    if (is(permission, SITE_READ)) {
      return canReadSite(user, site);
    } else if (is(permission, SITE_WRITE)) {
      return canWriteSite(user, site);
    } else if (is(permission, SITE_ENABLE)) {
      return canEnableSite(user, site);
    } else if (is(permission, SITE_DISABLE)) {
      return canDisableSite(user, site);
    }
    return false;
  }

  public boolean canReadSite(User user, Site site) {
    return hasPermission(user, GLOBAL_SITE_READ)
        && organisationPermissionEvaluator.isVisible(user, site.getOrganisation());
  }

  public boolean canWriteSite(User user, Site site) {
    return hasPermission(user, GLOBAL_SITE_WRITE)
        && organisationPermissionEvaluator.isVisible(user, site.getOrganisation());
  }

  public boolean canEnableSite(User user, Site site) {
    return !site.isEnabled()
        && hasPermission(user, GLOBAL_SITE_WRITE)
        && organisationPermissionEvaluator.isVisible(user, site.getOrganisation());
  }

  public boolean canDisableSite(User user, Site site) {
    return site.isEnabled()
        && hasPermission(user, GLOBAL_SITE_WRITE)
        && organisationPermissionEvaluator.isVisible(user, site.getOrganisation());
  }
}
