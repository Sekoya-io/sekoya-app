package sekoya.front.config;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SekoyaFrontSecurityConfiguration {

  @Bean
  @Order(1)
  public SecurityFilterChain consoleSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.securityMatcher("/console/**")
        .headers(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .anonymous(a -> a.authorities(CoreAuthorityConstants.ROLE_ANONYMOUS))
        .exceptionHandling(
            e ->
                e.accessDeniedPage("/console/access-denied/")
                    .authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/console/login/")))
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers("/console/login/**", "/console/access-denied/**")
                    .permitAll()
                    .requestMatchers("/console/**")
                    .hasAnyAuthority(CoreAuthorityConstants.ROLE_ADMIN))
        .build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain ressourcesSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.securityMatcher("/wicket/resource/**")
        .headers(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .exceptionHandling(e -> e.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(
                        regexMatcher(
                            "^/wicket/resource/sekoya.front.common.template.resources.js.[^/]+.*"),
                        regexMatcher(
                            "^/wicket/resource/sekoya.front.common.template.resources.styles.[^/]+.*"),
                        regexMatcher(
                            "^/wicket/resource/sekoya.front.common.template.resources.images.[^/]+.*"))
                    .hasAnyAuthority(CoreAuthorityConstants.ROLE_ANONYMOUS)
                    .requestMatchers(regexMatcher("^/wicket/resource/sekoya.front.[^/]+.*"))
                    .hasAnyAuthority(CoreAuthorityConstants.ROLE_AUTHENTICATED)
                    .requestMatchers("/wicket/resource/**")
                    .hasAnyAuthority(CoreAuthorityConstants.ROLE_ANONYMOUS))
        .build();
  }

  @Bean
  @Order(3)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.headers(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .anonymous(a -> a.authorities(CoreAuthorityConstants.ROLE_ANONYMOUS))
        .sessionManagement(
            s ->
                s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .sessionFixation()
                    .changeSessionId())
        .exceptionHandling(
            e ->
                e.accessDeniedPage("/access-denied/")
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/")))
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(
                        "/login/",
                        "/login/failure/",
                        "/access-denied/",
                        "/security/password/recovery/request/creation/",
                        "/security/password/recovery/request/reset/",
                        "/security/password/creation/",
                        "/security/password/reset/",
                        "/maintenance/")
                    .permitAll()
                    .requestMatchers(publicResourceMatchers())
                    .permitAll()
                    .requestMatchers("/**")
                    .hasAnyAuthority(CoreAuthorityConstants.ROLE_AUTHENTICATED))
        .build();
  }

  public static String[] publicResourceMatchers() {
    return new String[] {
      "/static/**",
      "/monitoring/**",
      "/webjars/**",
      "/apple-touch-icon.png",
      "/favicon.ico",
      "/favicon.svg",
      "/favicon-96x96.png",
      "/site.webmanifest",
      "/web-app-manifest-192x192.png",
      "/web-app-manifest-512x512.png"
    };
  }
}
