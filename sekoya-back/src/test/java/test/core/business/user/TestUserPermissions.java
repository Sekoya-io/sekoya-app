package test.core.business.user;

import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_USER_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_USER_WRITE;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.security.model.SecurityOptions;
import sekoya.back.security.service.ISecurityManagementService;
import sekoya.back.security.service.permission.UserPermissionEvaluatorImpl;

@ExtendWith(MockitoExtension.class)
public class TestUserPermissions {

  @Spy @InjectMocks private UserPermissionEvaluatorImpl userPermissionEvaluator;

  @Mock private ISecurityManagementService securityManagementService;

  @Nested
  class CanReadUser {
    @Test
    void canReadUser_userAdministrateurTechnique_userAdministrateurTechnique() {
      User authenticated = new User();
      authenticated.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canReadUser_userAdministrateurTechnique_userOrganisation() {
      User authenticated = new User();
      authenticated.setType(UserType.ORGANISATION);
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canReadUser_userOrganisation_sameUser() {
      User authenticated = new User();
      authenticated.setType(UserType.ORGANISATION);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, authenticated))
          .isTrue();
    }

    @Test
    void canReadUser_userOrganisation_hasPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.ORGANISATION);
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_READ);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canReadUser_userOrganisation_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.ORGANISATION);
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_READ);
      Assertions.assertThat(userPermissionEvaluator.canReadUser(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanWriteUser {
    @Test
    void canWriteUser_userAdministrateurTechnique_userAdministrateurTechnique() {
      User authenticated = new User();
      authenticated.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canWriteUser_userAdministrateurTechnique_userOrganisation() {
      User authenticated = new User();
      authenticated.setType(UserType.ORGANISATION);
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canWriteUser_userOrganisation_sameUser() {
      User authenticated = new User();
      authenticated.setType(UserType.ORGANISATION);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, authenticated))
          .isTrue();
    }

    @Test
    void canWriteUser_userOrganisation_hasPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.ORGANISATION);
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canWriteUser_userOrganisation_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.ORGANISATION);
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);
      Assertions.assertThat(userPermissionEvaluator.canWriteUser(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanEnableUser {
    @Test
    void canEnableUser_userAdministrateurTechnique_userEnable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canEnableUser_userOrganisation_userEnable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canEnableUser_userAdministrateurTechnique_userDisbale_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      targetUser.setEnabled(false);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canEnableUser_userAdministrateurTechnique_userDisable_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      targetUser.setEnabled(false);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canEnableUser_userOrganisation_userDisable_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      targetUser.setEnabled(false);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canEnableUser_userOrganisation_userDisable_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      targetUser.setEnabled(false);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canEnableUser(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanDisableUser {
    @Test
    void canDisableUser_userAdministrateurTechnique_userDisable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      targetUser.setEnabled(false);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_userOrganisation_userDisable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      targetUser.setEnabled(false);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_userAdministrateurTechnique_userActif_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canDisableUser_userAdministrateurTechnique_userActif_sameUser() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(targetUser, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_userAdministrateurTechnique_userActif_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_userOrganisation_userActif_withPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canDisableUser_userOrganisation_userActif_withPermission_sameUser() {
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(targetUser, targetUser))
          .isFalse();
    }

    @Test
    void canDisableUser_userOrganisation_userActif_withoutPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(userPermissionEvaluator.canDisableUser(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanUserEditPassword {

    @Test
    void canUserEditPassword_noUserConnected_passwordUserRecoveryEnabled() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserUpdate);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(null, targetUser)).isTrue();
    }

    @Test
    void canUserEditPassword_noUserConnected_passwordUserRecoveryDisable() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminUpdate);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(null, targetUser))
          .isFalse();
    }

    @Test
    void canUserEditPassword_userConnected_canWrite_passwordUserRecoveryEnabled() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      User authenticated = new User();
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserUpdate);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canUserEditPassword_UserConnected_canWrite_passwordUserRecoveryDisable() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      User authenticated = new User();
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canUserEditPassword_UserConnected_cannot_Write() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      User authenticated = new User();

      Mockito.doReturn(false).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(userPermissionEvaluator.canUserEditPassword(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanUserRecoveryPassword {

    @Test
    void canUserRecoveryPassword_noUserConnected_passwordUserRecoveryEnabled() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Assertions.assertThat(userPermissionEvaluator.canUserRecoveryPassword(null, targetUser))
          .isTrue();
    }

    @Test
    void canUserRecoveryPassword_noUserConnected_passwordUserRecoveryDisable() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminUpdate);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Assertions.assertThat(userPermissionEvaluator.canUserRecoveryPassword(null, targetUser))
          .isFalse();
    }

    @Test
    void canUserRecoveryPassword_userConnected_canWrite_passwordUserRecoveryEnabled() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      User authenticated = new User();
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(
              userPermissionEvaluator.canUserRecoveryPassword(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canUserRecoveryPassword_userConnected_canWrite_passwordUserRecoveryDisable() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      User authenticated = new User();
      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(
              userPermissionEvaluator.canUserRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canUserRecoveryPassword_userConnected_cannot_Write() {
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
      User authenticated = new User();

      Mockito.doReturn(false).when(userPermissionEvaluator).canWriteUser(authenticated, targetUser);
      Assertions.assertThat(
              userPermissionEvaluator.canUserRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanAdminRecoveryPassword {
    @Test
    void canAdminRecoveryPassword_passwordAdminRecoveryDisable() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordUserRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canAdminRecoveryPassword_passwordAdminRecoveryEnabled_userAdministrateurTechnique() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void
        canAdminRecoveryPassword_passwordAdminRecoveryEnabled_userAdministrateurTechnique_noPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ADMINISTRATEUR_TECHNIQUE);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(false).when(userPermissionEvaluator).hasRole(authenticated, ROLE_ADMIN);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }

    @Test
    void canAdminRecoveryPassword_passwordAdminRecoveryEnabled_userOrganisation() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(true)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isTrue();
    }

    @Test
    void canAdminRecoveryPassword_passwordAdminRecoveryEnabled_userOrganisation_noPermission() {
      User authenticated = new User();
      User targetUser = new User();
      targetUser.setType(UserType.ORGANISATION);

      SecurityOptions securityOptions =
          SecurityOptions.create(SecurityOptions.Builder::passwordAdminRecovery);
      Mockito.when(securityManagementService.getSecurityOptions(targetUser))
          .thenReturn(securityOptions);
      Mockito.doReturn(false)
          .when(userPermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_USER_WRITE);

      Assertions.assertThat(
              userPermissionEvaluator.canAdminRecoveryPassword(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanOpenAnnouncement {
    @Test
    void canOpenAnnouncement_announcementClose_sameUser() {
      User user = new User();
      Assertions.assertThat(userPermissionEvaluator.canOpenAnnouncement(user, user)).isTrue();
    }

    @Test
    void canOpenAnnouncement_announcementOpen_sameUser() {
      User user = new User();
      Assertions.assertThat(userPermissionEvaluator.canOpenAnnouncement(user, user)).isTrue();
    }

    @Test
    void canOpenAnnouncement_differentUser() {
      User authenticated = new User();
      User targetUser = new User();
      Assertions.assertThat(userPermissionEvaluator.canOpenAnnouncement(authenticated, targetUser))
          .isFalse();
    }
  }

  @Nested
  class CanCloseAnnouncement {
    @Test
    void canCloseAnnouncement_announcementOpen_sameUser() {
      User user = new User();
      Assertions.assertThat(userPermissionEvaluator.canCloseAnnouncement(user, user)).isTrue();
    }

    @Test
    void canCloseAnnouncement_announcementClose_sameUser() {
      User user = new User();
      Assertions.assertThat(userPermissionEvaluator.canCloseAnnouncement(user, user)).isTrue();
    }

    @Test
    void canCloseAnnouncement_differentUser() {
      User authenticated = new User();
      User targetUser = new User();
      Assertions.assertThat(userPermissionEvaluator.canCloseAnnouncement(authenticated, targetUser))
          .isFalse();
    }
  }
}
