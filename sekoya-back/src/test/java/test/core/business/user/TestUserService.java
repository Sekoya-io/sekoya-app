package test.core.business.user;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ANNOUNCEMENT_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ANNOUNCEMENT_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.USER_EDIT_PASSWORD;

import com.google.common.collect.ImmutableSortedSet;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.DateUtil;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import sekoya.back.business.common.model.EmailAddress;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.model.Role.RoleEnumKey;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.business.user.service.controller.IUserControllerService;
import test.core.AbstractSekoyaTestCase;
import test.core.config.SekoyaBackSpringBootTest;

@SekoyaBackSpringBootTest
class TestUserService extends AbstractSekoyaTestCase {

  @Autowired private IUserControllerService userControllerService;

  @Nested
  class saveUser {
    @WithUserDetails(
        value = USER_ADMINISTRATEUR_TECHNIQUE_USERNAME,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveAdministrateurTechnique() throws SecurityServiceException, ServiceException {
      User user =
          entityDatabaseHelper.createUser(
              u -> {
                u.setUsername("test");
                u.setFirstName("firstname");
                u.setLastName("lastname");
              },
              false);

      userControllerService.saveUserAdministrateurTechnique(user, USER_EDIT_PASSWORD);
      entityManagerReset();
      User userBdd = userService.getById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.ADMINISTRATEUR_TECHNIQUE);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getCreation().getDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getModification().getDate());
      Assertions.assertThat(new Date())
          .isInSameDayAs(userBdd.getPasswordInformation().getLastUpdateDate());
    }

    @WithUserDetails(
        value = USER_ORGANISATION_USERNAME_WITH_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveUserOrganisation() throws SecurityServiceException, ServiceException {
      User user =
          entityDatabaseHelper.createUser(
              u -> {
                u.setEmailAddress(new EmailAddress("test@test.fr"));
                u.setFirstName("firstname");
                u.setLastName("lastname");
                u.setType(null);
              },
              false);

      userControllerService.saveUserOrganisation(user, USER_EDIT_PASSWORD);
      entityManagerReset();
      User userBdd = userService.getById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test@test.fr");
      Assertions.assertThat(userBdd.getEmailAddress().getValue()).isEqualTo("test@test.fr");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.ORGANISATION);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getCreation().getDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getModification().getDate());
      Assertions.assertThat(new Date())
          .isInSameDayAs(userBdd.getPasswordInformation().getLastUpdateDate());
    }

    @WithUserDetails(
        value = USER_ADMINISTRATEUR_TECHNIQUE_USERNAME,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Sql(scripts = {"/scripts/user-test.sql"})
    @Test
    void testUpdateUser() throws SecurityServiceException, ServiceException {
      entityManagerReset();
      User user = userService.getById(-4L);
      user.setFirstName("updatedFirstname");
      user.setLastName("updatedFirstname");
      userControllerService.saveUserAdministrateurTechnique(user, "newPassword");
      entityManagerReset();
      User userBdd = userService.getById(user.getId());
      Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
      Assertions.assertThat(userBdd.getFirstName()).isEqualTo("updatedFirstname");
      Assertions.assertThat(userBdd.getLastName()).isEqualTo("updatedFirstname");
      Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.ADMINISTRATEUR_TECHNIQUE);
      Assertions.assertThat(userBdd.getPasswordHash()).startsWith("{bcrypt}");
      Assertions.assertThat(userBdd.isEnabled()).isTrue();
      Assertions.assertThat(userBdd.getLocale()).isEqualTo(Locale.FRENCH);
      Assertions.assertThat(DateUtil.parse("2024-01-01"))
          .isInSameDayAs(userBdd.getCreation().getDate());
      Assertions.assertThat(new Date()).isInSameDayAs(userBdd.getModification().getDate());
    }

    @WithUserDetails(
        value = USER_ORGANISATION_USERNAME_WITH_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void
        testSaveUserAdministrateurTechnique_userOrganisationAuthenticate_throwAuthorizationDeniedException() {
      Assertions.assertThatThrownBy(
              () ->
                  userControllerService.saveUserAdministrateurTechnique(
                      entityDatabaseHelper.createUser(null, false), USER_EDIT_PASSWORD))
          .isInstanceOf(AuthorizationDeniedException.class);
    }

    @WithUserDetails(
        value = USER_ADMINISTRATEUR_TECHNIQUE_USERNAME,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void testSaveUserOrganisation_userAdministrateurTechniqueAuthenticate_doesNotThrowException() {
      Assertions.assertThatCode(
              () ->
                  userControllerService.saveUserOrganisation(
                      entityDatabaseHelper.createUser(null, false), USER_EDIT_PASSWORD))
          .doesNotThrowAnyException();
    }

    @WithUserDetails(
        value = USER_ORGANISATION_USERNAME_WITHOUT_PERMISSIONS,
        setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void
        testSaveUserOrganisation_userWithoutPermissionsAuthenticate_throwAuthorizationDeniedException() {
      Assertions.assertThatThrownBy(
              () ->
                  userControllerService.saveUserOrganisation(
                      entityDatabaseHelper.createUser(null, false), USER_EDIT_PASSWORD))
          .isInstanceOf(AuthorizationDeniedException.class);
    }
  }

  // TODO : voir avec RFO
  @Disabled
  @Test
  void testListUser() throws ServiceException, SecurityServiceException {
    Role role1 =
        entityDatabaseHelper.createRole(
            r -> {
              r.setEnumKey(RoleEnumKey.ORGANISATION);
              r.setPermissions(
                  ImmutableSortedSet.of(GLOBAL_REFERENCE_DATA_READ, GLOBAL_REFERENCE_DATA_WRITE));
            },
            true);

    Role role2 =
        entityDatabaseHelper.createRole(
            r -> {
              r.setEnumKey(RoleEnumKey.ADMINISTRATEUR_FONCTIONNEL);
              r.setPermissions(
                  ImmutableSortedSet.of(GLOBAL_ANNOUNCEMENT_READ, GLOBAL_ANNOUNCEMENT_WRITE));
            },
            true);

    User user =
        entityDatabaseHelper.createUser(
            u -> {
              u.setUsername("test");
              u.setFirstName("firstname");
              u.setLastName("lastname");
              u.setType(UserType.ORGANISATION);
              u.setRoles(ImmutableSortedSet.of(role1, role2));
            },
            true);

    entityManagerReset();
    List<User> userList = userService.list();

    Assertions.assertThat(userList).size().isEqualTo(4);
    User userBdd = userService.getById(user.getId());
    Assertions.assertThat(userBdd.getUsername()).isEqualTo("test");
    Assertions.assertThat(userBdd.getFirstName()).isEqualTo("firstname");
    Assertions.assertThat(userBdd.getLastName()).isEqualTo("lastname");
    Assertions.assertThat(userBdd.getType()).isEqualTo(UserType.ORGANISATION);
    Assertions.assertThat(userBdd.getRoles()).containsExactlyInAnyOrder(role1, role2);
  }
}
