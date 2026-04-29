package test.core.security;

import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ANONYMOUS;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_AUTHENTICATED;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ANNOUNCEMENT_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ANNOUNCEMENT_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;

import com.google.common.collect.ImmutableSortedSet;
import igloo.security.ICoreUserDetailsService;
import igloo.security.UserDetails;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.model.NamedPermission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import sekoya.back.business.role.service.IRoleService;
import sekoya.back.business.user.model.atomic.UserType;
import test.core.AbstractSekoyaTestCase;
import test.core.config.SekoyaBackSpringBootTest;

@SekoyaBackSpringBootTest
public class TestUserDetailsService extends AbstractSekoyaTestCase {

  @Autowired private ICoreUserDetailsService userDetailsService;

  @Autowired private IRoleService roleService;

  @Test
  void testGetAuthoritiesAndPermissions_technicalUser()
      throws SecurityServiceException, ServiceException {

    UserDetails userDetails = userDetailsService.loadUserByUsername(ADMIN_USERNAME);
    Assertions.assertThat(userDetails).isNotNull();
    Assertions.assertThat(userDetails.getUsername()).isEqualTo(ADMIN_USERNAME);

    Assertions.assertThat(userDetails.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactlyInAnyOrder(ROLE_ADMIN, ROLE_ANONYMOUS, ROLE_AUTHENTICATED);
    List<String> permissions =
        userDetails.getPermissions().stream()
            .filter(permission -> permission instanceof NamedPermission)
            .map(permission -> ((NamedPermission) permission).getName())
            .toList();

    Assertions.assertThat(permissions)
        .containsExactlyInAnyOrder(
            roleService.list().stream()
                .flatMap(role -> role.getPermissions().stream())
                .toArray(String[]::new));
  }

  @Test
  void testGetAuthoritiesAndPermissions_basicUser()
      throws SecurityServiceException, ServiceException {
    entityDatabaseHelper.createRole(
        r ->
            r.setPermissions(
                ImmutableSortedSet.of(GLOBAL_REFERENCE_DATA_READ, GLOBAL_REFERENCE_DATA_WRITE)),
        true);

    addPermissions(
        entityDatabaseHelper.createUser(
            u -> {
              u.setUsername("username");
              u.setType(UserType.BASIC);
            },
            true),
        GLOBAL_ANNOUNCEMENT_READ,
        GLOBAL_ANNOUNCEMENT_WRITE);

    entityManagerReset();
    UserDetails userDetails = userDetailsService.loadUserByUsername("username");
    Assertions.assertThat(userDetails).isNotNull();
    Assertions.assertThat(userDetails.getUsername()).isEqualTo("username");

    Assertions.assertThat(userDetails.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactlyInAnyOrder(ROLE_ANONYMOUS, ROLE_AUTHENTICATED);
    List<String> permissions =
        userDetails.getPermissions().stream()
            .filter(permission -> permission instanceof NamedPermission)
            .map(permission -> ((NamedPermission) permission).getName())
            .toList();

    Assertions.assertThat(permissions)
        .containsExactlyInAnyOrder(GLOBAL_ANNOUNCEMENT_READ, GLOBAL_ANNOUNCEMENT_WRITE);
  }
}
