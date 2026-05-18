package sekoya.back.security.service.permission;

import org.iglooproject.jpa.security.service.AbstractCorePermissionEvaluator;
import org.iglooproject.jpa.util.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;
import sekoya.back.business.announcement.model.Announcement;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.referencedata.model.ReferenceData;
import sekoya.back.business.user.model.User;

public class SekoyaPermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

  @Autowired private IOrganisationPermissionEvaluator organisationPermissionEvaluator;

  @Autowired private IUserPermissionEvaluator userPermissionEvaluator;

  @Autowired private IReferenceDataPermissionEvaluator referenceDataPermissionEvaluator;

  @Autowired private IAnnouncementPermissionEvaluator announcementPermissionEvaluator;

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
      case User targetUser -> userPermissionEvaluator.hasPermission(user, targetUser, permission);
      case ReferenceData<?> referenceData ->
          referenceDataPermissionEvaluator.hasPermission(user, referenceData, permission);
      case Announcement announcement ->
          announcementPermissionEvaluator.hasPermission(user, announcement, permission);
      case null, default -> false;
    };
  }
}
