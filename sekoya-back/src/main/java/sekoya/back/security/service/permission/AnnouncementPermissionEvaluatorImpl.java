package sekoya.back.security.service.permission;

import static sekoya.back.security.model.SekoyaPermissionConstants.ANNOUNCEMENT_REMOVE;
import static sekoya.back.security.model.SekoyaPermissionConstants.ANNOUNCEMENT_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ANNOUNCEMENT_WRITE;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import sekoya.back.business.announcement.model.Announcement;
import sekoya.back.business.user.model.User;

@Service
public class AnnouncementPermissionEvaluatorImpl
    extends AbstractGenericPermissionEvaluator<Announcement>
    implements IAnnouncementPermissionEvaluator {

  @Override
  public boolean hasPermission(User user, Announcement announcement, Permission permission) {
    if (is(permission, ANNOUNCEMENT_WRITE)) {
      return canWriteAnnouncement(user);
    } else if (is(permission, ANNOUNCEMENT_REMOVE)) {
      return canRemoveAnnouncement(user);
    }
    return false;
  }

  @VisibleForTesting
  public boolean canWriteAnnouncement(User user) {
    return hasPermission(user, GLOBAL_ANNOUNCEMENT_WRITE);
  }

  @VisibleForTesting
  public boolean canRemoveAnnouncement(User user) {
    return hasPermission(user, GLOBAL_ANNOUNCEMENT_WRITE);
  }
}
