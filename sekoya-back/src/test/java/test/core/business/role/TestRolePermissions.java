package test.core.business.role;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ROLE_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ROLE_WRITE;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.security.service.permission.RolePermissionEvaluatorImpl;

@ExtendWith(MockitoExtension.class)
public class TestRolePermissions {

  @Spy @InjectMocks private RolePermissionEvaluatorImpl rolePermissionEvaluator;

  @Nested
  class CanReadRole {
    @Test
    void canReadRole_withPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);
      Assertions.assertThat(rolePermissionEvaluator.canRead(authenticated)).isTrue();
    }

    @Test
    void canReadRole_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);
      Assertions.assertThat(rolePermissionEvaluator.canRead(authenticated)).isFalse();
    }
  }

  @Nested
  class CanWriteRole {
    @Test
    void canWriteRole_withPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);

      Mockito.doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);

      Assertions.assertThat(rolePermissionEvaluator.canWrite(authenticated)).isTrue();
    }

    @Test
    void canWriteRole_noPermission() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);

      Mockito.lenient()
          .doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);

      Assertions.assertThat(rolePermissionEvaluator.canWrite(authenticated)).isFalse();
    }

    @Test
    void canWriteRole_onlyRead() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);

      Mockito.doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);

      Assertions.assertThat(rolePermissionEvaluator.canWrite(authenticated)).isFalse();
    }

    @Test
    void canWriteRole_onlyWrite() {
      User authenticated = new User();
      authenticated.setType(UserType.BASIC);

      Mockito.doReturn(false)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_READ);

      Mockito.lenient()
          .doReturn(true)
          .when(rolePermissionEvaluator)
          .hasPermission(authenticated, GLOBAL_ROLE_WRITE);

      Assertions.assertThat(rolePermissionEvaluator.canWrite(authenticated)).isFalse();
    }
  }
}
