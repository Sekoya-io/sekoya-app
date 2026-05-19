package sekoya.back.config;

import static sekoya.back.property.SekoyaBackPropertyIds.SECURITY_PASSWORD_LENGTH_MAX;
import static sekoya.back.property.SekoyaBackPropertyIds.SECURITY_PASSWORD_LENGTH_MIN;
import static sekoya.back.property.SekoyaBackPropertyIds.SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS;

import com.google.common.collect.ImmutableMap;
import org.iglooproject.jpa.security.service.AuthenticationUsernameComparison;
import org.iglooproject.jpa.security.service.ICorePermissionEvaluator;
import org.iglooproject.jpa.security.service.NamedPermissionFactory;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.acls.domain.PermissionFactory;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.security.model.SecurityOptions;
import sekoya.back.security.model.SekoyaPermission;
import sekoya.back.security.service.ISecurityManagementService;
import sekoya.back.security.service.ISekoyaAuthenticationService;
import sekoya.back.security.service.ISekoyaSecurityService;
import sekoya.back.security.service.ISekoyaUserDetailsService;
import sekoya.back.security.service.SecurityManagementServiceImpl;
import sekoya.back.security.service.SekoyaAuthenticationServiceImpl;
import sekoya.back.security.service.SekoyaSecurityServiceImpl;
import sekoya.back.security.service.SekoyaUserDetailsServiceImpl;
import sekoya.back.security.service.permission.IAnnouncementPermissionEvaluator;
import sekoya.back.security.service.permission.IOrganisationPermissionEvaluator;
import sekoya.back.security.service.permission.IReferenceDataPermissionEvaluator;
import sekoya.back.security.service.permission.ISitePermissionEvaluator;
import sekoya.back.security.service.permission.IUserPermissionEvaluator;
import sekoya.back.security.service.permission.SekoyaPermissionEvaluator;

@Configuration
public class SekoyaBackSecurityConfiguration {

  @Bean
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  public ICorePermissionEvaluator permissionEvaluator(
      IOrganisationPermissionEvaluator organisationPermissionEvaluator,
      ISitePermissionEvaluator sitePermissionEvaluator,
      IUserPermissionEvaluator userPermissionEvaluator,
      IReferenceDataPermissionEvaluator referenceDataPermissionEvaluator,
      IAnnouncementPermissionEvaluator announcementPermissionEvaluator) {
    return new SekoyaPermissionEvaluator(
        organisationPermissionEvaluator,
        sitePermissionEvaluator,
        userPermissionEvaluator,
        referenceDataPermissionEvaluator,
        announcementPermissionEvaluator);
  }

  @Bean
  public ISekoyaAuthenticationService authenticationService() {
    return new SekoyaAuthenticationServiceImpl();
  }

  @Bean
  public ISekoyaUserDetailsService userDetailsService(
      AuthenticationUsernameComparison authenticationUsernameComparison) {
    SekoyaUserDetailsServiceImpl userDetailsService = new SekoyaUserDetailsServiceImpl();
    userDetailsService.setAuthenticationUsernameComparison(authenticationUsernameComparison);
    return userDetailsService;
  }

  @Bean
  public AuthenticationUsernameComparison authenticationUsernameComparison() {
    return AuthenticationUsernameComparison.CASE_SENSITIVE;
  }

  @Bean
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  public ISekoyaSecurityService securityService() {
    return new SekoyaSecurityServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  // TODO igloo-boot: replace with a customizer for permission list
  public PermissionFactory permissionFactory() {
    return new NamedPermissionFactory(SekoyaPermission.ALL);
  }

  /**
   * Password max byte length cannot be more than 72 bytes.
   *
   * @see sekoya.back.business.user.service.business.UserServiceImpl#setPasswords
   * @see org.springframework.security.crypto.bcrypt.BCrypt#hashpw(byte[], String, boolean)
   */
  @Bean
  public ISecurityManagementService securityManagementService(IPropertyService propertyService) {
    int passwordLengthMin = propertyService.get(SECURITY_PASSWORD_LENGTH_MIN);
    int passwordLengthMax = propertyService.get(SECURITY_PASSWORD_LENGTH_MAX);

    return new SecurityManagementServiceImpl(
        SecurityOptions.create(
            securityOptions ->
                securityOptions
                    .passwordUserRecovery()
                    .passwordUserUpdate()
                    .passwordRules(
                        rules ->
                            rules
                                .minMaxLength(passwordLengthMin, passwordLengthMax)
                                .forbiddenUsername()
                                .forbiddenPasswords(
                                    propertyService.get(
                                        SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS)))),
        ImmutableMap.<UserType, SecurityOptions>builder()
            .put(
                UserType.ADMINISTRATEUR_TECHNIQUE,
                SecurityOptions.create(
                    securityOptions ->
                        securityOptions
                            .passwordAdminRecovery()
                            .passwordUserRecovery()
                            .passwordUserUpdate()
                            .passwordRules(
                                rules ->
                                    rules
                                        .minMaxLength(passwordLengthMin, passwordLengthMax)
                                        .forbiddenUsername()
                                        .forbiddenPasswords(
                                            propertyService.get(
                                                SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS)))))
            .put(
                UserType.ADMINISTRATEUR_FONCTIONNEL,
                SecurityOptions.create(
                    securityOptions ->
                        securityOptions
                            .passwordUserRecovery()
                            .passwordUserUpdate()
                            .passwordRules(
                                rules ->
                                    rules
                                        .minMaxLength(passwordLengthMin, passwordLengthMax)
                                        .forbiddenUsername()
                                        .forbiddenPasswords(
                                            propertyService.get(
                                                SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS)))))
            .put(
                UserType.ORGANISATION,
                SecurityOptions.create(
                    securityOptions ->
                        securityOptions
                            .passwordUserRecovery()
                            .passwordUserUpdate()
                            .passwordRules(
                                rules ->
                                    rules
                                        .minMaxLength(passwordLengthMin, passwordLengthMax)
                                        .forbiddenUsername()
                                        .forbiddenPasswords(
                                            propertyService.get(
                                                SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS)))))
            .build());
  }
}
