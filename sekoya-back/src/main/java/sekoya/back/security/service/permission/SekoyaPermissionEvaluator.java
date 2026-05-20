package sekoya.back.security.service.permission;

import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.jpa.util.HibernateUtils;
import org.springframework.security.acls.model.Permission;
import sekoya.back.business.announcement.model.Announcement;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.referencedata.model.ReferenceData;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.user.model.User;

public class SekoyaPermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

  private final IOrganisationPermissionEvaluator organisationPermissionEvaluator;

  private final ISitePermissionEvaluator sitePermissionEvaluator;

  private final IProcessusPermissionEvaluator processusPermissionEvaluator;

  private final IUserPermissionEvaluator userPermissionEvaluator;

  private final IReferenceDataPermissionEvaluator referenceDataPermissionEvaluator;

  private final IAnnouncementPermissionEvaluator announcementPermissionEvaluator;

  public SekoyaPermissionEvaluator(
      IOrganisationPermissionEvaluator organisationPermissionEvaluator,
      ISitePermissionEvaluator sitePermissionEvaluator,
      IProcessusPermissionEvaluator processusPermissionEvaluator,
      IUserPermissionEvaluator userPermissionEvaluator,
      IReferenceDataPermissionEvaluator referenceDataPermissionEvaluator,
      IAnnouncementPermissionEvaluator announcementPermissionEvaluator) {
    super();
    this.organisationPermissionEvaluator = organisationPermissionEvaluator;
    this.sitePermissionEvaluator = sitePermissionEvaluator;
    this.processusPermissionEvaluator = processusPermissionEvaluator;
    this.userPermissionEvaluator = userPermissionEvaluator;
    this.referenceDataPermissionEvaluator = referenceDataPermissionEvaluator;
    this.announcementPermissionEvaluator = announcementPermissionEvaluator;
  }

  @Override
  protected boolean hasPermission(User user, Object targetDomainObject, Permission permission) {
    if (targetDomainObject != null) {
      targetDomainObject = HibernateUtils.unwrap(targetDomainObject); // NOSONAR
    }

    if (user != null) {
      user = HibernateUtils.unwrap(user); // NOSONAR
    }

    return switch (targetDomainObject) {
      case Organisation organisation ->
          organisationPermissionEvaluator.hasPermission(user, organisation, permission);
      case Site site -> sitePermissionEvaluator.hasPermission(user, site, permission);
      case Processus processus ->
          processusPermissionEvaluator.hasPermission(user, processus, permission);
      case User targetUser -> userPermissionEvaluator.hasPermission(user, targetUser, permission);
      case ReferenceData<?> referenceData ->
          referenceDataPermissionEvaluator.hasPermission(user, referenceData, permission);
      case Announcement announcement ->
          announcementPermissionEvaluator.hasPermission(user, announcement, permission);
      case null, default -> false;
    };
  }
}
