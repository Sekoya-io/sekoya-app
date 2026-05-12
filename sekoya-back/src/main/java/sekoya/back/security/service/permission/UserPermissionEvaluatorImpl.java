package sekoya.back.security.service.permission;

import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;
import static sekoya.back.security.model.SekoyaPermissionConstants.ADMIN_EDIT_PASSWORD;
import static sekoya.back.security.model.SekoyaPermissionConstants.ADMIN_RECOVERY_PASSWORD;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_USER_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_USER_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_ADMINISTATEUR_FONCTIONNEL_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_ADMINISTATEUR_TECHNIQUE_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_CLOSE_ANNONCEMENT;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_DISABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_EDIT_PASSWORD;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_ENABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_OPEN_ANNONCEMENT;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_ORGANISATION_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_RECOVERY_PASSWORD;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_WRITE;

import com.google.common.annotations.VisibleForTesting;
import java.util.Objects;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.predicate.UserPredicates;
import sekoya.back.security.service.ISecurityManagementService;

@Service
public class UserPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<User>
    implements IUserPermissionEvaluator {

  private final ISecurityManagementService securityManagementService;

  @Autowired
  public UserPermissionEvaluatorImpl(ISecurityManagementService securityManagementService) {
    this.securityManagementService = securityManagementService;
  }

  @Override
  public boolean hasPermission(User user, User targetUser, Permission permission) {
    if (is(permission, USER_READ)) {
      return canReadUser(user, targetUser);
    } else if (is(permission, USER_WRITE)) {
      return canWriteUser(user, targetUser);
    } else if (is(permission, USER_ADMINISTATEUR_TECHNIQUE_WRITE)) {
      return canWriteUserAdministrateurTechnique(user);
    } else if (is(permission, USER_ADMINISTATEUR_FONCTIONNEL_WRITE)) {
      return canWriteUserAdministrateurFonctionnel(user);
    } else if (is(permission, USER_ORGANISATION_WRITE)) {
      return canWriteUserOrganisation(user, targetUser);
    } else if (is(permission, USER_ENABLE)) {
      return canEnableUser(user, targetUser);
    } else if (is(permission, USER_DISABLE)) {
      return canDisableUser(user, targetUser);
    } else if (is(permission, USER_EDIT_PASSWORD)) {
      return canUserEditPassword(user, targetUser);
    } else if (is(permission, USER_RECOVERY_PASSWORD)) {
      return canUserRecoveryPassword(user, targetUser);
    } else if (is(permission, ADMIN_EDIT_PASSWORD)) {
      return canAdminEditPassword(user, targetUser);
    } else if (is(permission, ADMIN_RECOVERY_PASSWORD)) {
      return canAdminRecoveryPassword(user, targetUser);
    } else if (is(permission, USER_OPEN_ANNONCEMENT)) {
      return canOpenAnnouncement(user, targetUser);
    } else if (is(permission, USER_CLOSE_ANNONCEMENT)) {
      return canCloseAnnouncement(user, targetUser);
    }
    return false;
  }

  @VisibleForTesting
  public boolean canReadUser(User user, User targetUser) {
    return switch (targetUser.getType()) {
      case ADMINISTRATEUR_TECHNIQUE -> canReadUserAdministrateurTechnique(user);
      case ADMINISTRATEUR_FONCTIONNEL -> canReadUserAdministrateurFonctionnel(user);
      case ORGANISATION -> canReadUserOrganisation(user, targetUser);
      default -> throw new IllegalSwitchValueException(user.getType());
    };
  }

  private boolean canReadUserAdministrateurTechnique(User user) {
    return hasRole(user, ROLE_ADMIN);
  }

  private boolean canReadUserAdministrateurFonctionnel(User user) {
    return hasPermission(user, GLOBAL_USER_READ);
  }

  private boolean canReadUserOrganisation(User user, User targetUser) {
    return Objects.equals(user, targetUser) || hasPermission(user, GLOBAL_USER_READ);
  }

  @VisibleForTesting
  public boolean canWriteUser(User user, User targetUser) {
    return switch (targetUser.getType()) {
      case ADMINISTRATEUR_TECHNIQUE -> canWriteUserAdministrateurTechnique(user);
      case ADMINISTRATEUR_FONCTIONNEL -> canWriteUserAdministrateurFonctionnel(user);
      case ORGANISATION -> canWriteUserOrganisation(user, targetUser);
      default -> throw new IllegalSwitchValueException(user.getType());
    };
  }

  private boolean canWriteUserAdministrateurTechnique(User user) {
    return hasRole(user, ROLE_ADMIN);
  }

  private boolean canWriteUserAdministrateurFonctionnel(User user) {
    return hasPermission(user, GLOBAL_USER_WRITE);
  }

  private boolean canWriteUserOrganisation(User user, User targetUser) {
    return Objects.equals(user, targetUser) || hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canEnableUser(User user, User targetUser) {
    if (targetUser.isEnabled()) {
      return false;
    }

    return UserPredicates.administrateurTechnique().apply(targetUser)
        ? hasRole(user, ROLE_ADMIN)
        : hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canDisableUser(User user, User targetUser) {
    if (Objects.equals(user, targetUser) || !targetUser.isEnabled()) {
      return false;
    }

    return UserPredicates.administrateurTechnique().apply(targetUser)
        ? hasRole(user, ROLE_ADMIN)
        : hasPermission(user, GLOBAL_USER_WRITE);
  }

  private boolean canAdminEditPassword(User user, User targetUser) {
    if (!securityManagementService.getSecurityOptions(targetUser).isPasswordAdminUpdateEnabled()) {
      return false;
    }

    return UserPredicates.administrateurTechnique().apply(targetUser)
        ? hasRole(user, ROLE_ADMIN)
        : hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canUserEditPassword(User user, User targetUser) {
    if (user != null && !canWriteUser(user, targetUser)) {
      return false;
    }
    return securityManagementService.getSecurityOptions(targetUser).isPasswordUserUpdateEnabled();
  }

  @VisibleForTesting
  public boolean canUserRecoveryPassword(User user, User targetUser) {
    if (user != null && !canWriteUser(user, targetUser)) {
      return false;
    }
    return securityManagementService.getSecurityOptions(targetUser).isPasswordUserRecoveryEnabled();
  }

  @VisibleForTesting
  public boolean canAdminRecoveryPassword(User user, User targetUser) {
    if (!securityManagementService
        .getSecurityOptions(targetUser)
        .isPasswordAdminRecoveryEnabled()) {
      return false;
    }

    return UserPredicates.administrateurTechnique().apply(targetUser)
        ? hasRole(user, ROLE_ADMIN)
        : hasPermission(user, GLOBAL_USER_WRITE);
  }

  @VisibleForTesting
  public boolean canOpenAnnouncement(User user, User targetUser) {
    return Objects.equals(user, targetUser);
  }

  @VisibleForTesting
  public boolean canCloseAnnouncement(User user, User targetUser) {
    return Objects.equals(user, targetUser);
  }
}
