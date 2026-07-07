package sekoya.back.business.user.service.business;

import static org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl.EMPTY_PASSWORD_HASH;

import com.google.common.base.Preconditions;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sekoya.back.business.common.model.EmailAddress;
import sekoya.back.business.history.model.atomic.HistoryLogEventType;
import sekoya.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.history.service.IHistoryLogService;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.model.Role.RoleEnumKey;
import sekoya.back.business.role.service.IRoleService;
import sekoya.back.business.user.dao.IUserDao;
import sekoya.back.business.user.difference.service.IUserDifferenceService;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import sekoya.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.business.user.predicate.UserPredicates;
import sekoya.back.security.service.ISecurityManagementService;
import sekoya.back.security.service.ISekoyaAuthenticationService;

@Service("userService")
public class UserServiceImpl extends GenericEntityServiceImpl<Long, User> implements IUserService {

  private final IUserDao dao;
  private final IUserDifferenceService userDifferenceService;
  private final IHistoryLogService historyLogService;
  private final IHistoryEventSummaryService historyEventSummaryService;
  private final IPropertyService propertyService;
  private final ISekoyaAuthenticationService authenticationService;
  private final ISecurityManagementService securityManagementService;
  private final IRoleService roleService;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(
      IUserDao dao,
      IUserDifferenceService userDifferenceService,
      @Lazy IHistoryLogService historyLogService,
      @Lazy IHistoryEventSummaryService historyEventSummaryService,
      @Lazy ISekoyaAuthenticationService authenticationService,
      IPropertyService propertyService,
      @Lazy ISecurityManagementService securityManagementService,
      PasswordEncoder passwordEncoder,
      IRoleService roleService) {
    super(dao);
    this.dao = dao;
    this.userDifferenceService = userDifferenceService;
    this.historyLogService = historyLogService;
    this.historyEventSummaryService = historyEventSummaryService;
    this.propertyService = propertyService;
    this.authenticationService = authenticationService;
    this.securityManagementService = securityManagementService;
    this.passwordEncoder = passwordEncoder;
    this.roleService = roleService;
  }

  @Override
  protected void createEntity(User user) throws ServiceException, SecurityServiceException {
    user.setEnabled(true);

    historyEventSummaryService.refresh(user.getCreation());
    historyEventSummaryService.refresh(user.getModification());

    super.createEntity(user);

    historyLogService.logWithDifferences(
        HistoryLogEventType.CREATE,
        user,
        HistoryLogAdditionalInformationBean.empty(),
        userDifferenceService);
  }

  @Override
  protected void updateEntity(User user) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(user.getModification());

    super.updateEntity(user);

    historyLogService.logWithDifferences(
        HistoryLogEventType.UPDATE,
        user,
        HistoryLogAdditionalInformationBean.empty(),
        userDifferenceService);
  }

  @Override
  public void saveUserOrganisation(User user, String password)
      throws SecurityServiceException, ServiceException {
    Objects.requireNonNull(user);
    Objects.requireNonNull(user.getUserOrganisation());

    user.setUsername(user.getEmailAddress().getValue());
    user.setType(UserType.ORGANISATION);
    user.getUserOrganisation().setUser(user);
    User author = getAuthenticatedUser();
    addRoleForNewUser(user, RoleEnumKey.ORGANISATION);
    saveUser(user, author, password);
  }

  @Override
  public void saveUserAdministrateurFonctionnel(User user, String password)
      throws SecurityServiceException, ServiceException {
    user.setUsername(user.getEmailAddress().getValue());
    user.setType(UserType.ADMINISTRATEUR_FONCTIONNEL);
    User author = getAuthenticatedUser();
    addRoleForNewUser(user, RoleEnumKey.ADMINISTRATEUR_FONCTIONNEL);
    saveUser(user, author, password);
  }

  @Override
  public void saveUserAdministrateurTechnique(User user, String password)
      throws SecurityServiceException, ServiceException {
    user.setType(UserType.ADMINISTRATEUR_TECHNIQUE);
    User author = getAuthenticatedUser();
    saveUser(user, author, password);
  }

  private void addRoleForNewUser(User user, RoleEnumKey roleEnumKey) throws ServiceException {
    if (!user.isNew()
        || UserPredicates.administrateurTechnique().apply(user)
        || roleEnumKey == null) {
      return;
    }

    Role role = roleService.getByEnumKey(roleEnumKey);
    if (role == null) {
      throw new ServiceException("Aucun rôle pour l'enumkey '%s'".formatted(roleEnumKey));
    }

    user.addRole(role);
  }

  private void saveUser(User user, User author, String password)
      throws SecurityServiceException, ServiceException {
    Objects.requireNonNull(user);

    if (user.getLocale() == null) {
      user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
    }

    if (user.isNew()) {
      create(user);
      if (StringUtils.hasText(password)) {
        securityManagementService.updatePassword(user, password, author);
      } else {
        securityManagementService.initiatePasswordRecoveryRequest(
            user,
            UserPasswordRecoveryRequestType.CREATION,
            UserPasswordRecoveryRequestInitiator.ADMIN,
            author);
      }
    } else {
      update(user);
    }
  }

  /**
   * Encode and set Password to user.
   *
   * <p>check that Password cannot be more than 72 bytes.
   *
   * @see <a href="https://spring.io/security/cve-2025-22228">CVE-2025-22228</a>
   * @see org.springframework.security.crypto.bcrypt.BCrypt#hashpw(byte[], String, boolean)
   */
  @Override
  public void onSignIn(User user) throws ServiceException, SecurityServiceException {
    historyLogService.log(
        HistoryLogEventType.SIGN_IN, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void onSignInFail(User user) throws ServiceException, SecurityServiceException {
    historyLogService.log(
        HistoryLogEventType.SIGN_IN_FAIL, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void setPassword(User user, String rawPassword)
      throws ServiceException, SecurityServiceException {
    Preconditions.checkArgument(StringUtils.hasText(rawPassword));

    if (rawPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new SecurityServiceException("password cannot be more than 72 bytes");
    }

    user.setPasswordHash(passwordEncoder.encode(rawPassword));
    update(user);
  }

  @Override
  public boolean hasPassword(User user) {
    Objects.requireNonNull(user);
    return !Objects.equals(user.getPasswordHash(), EMPTY_PASSWORD_HASH);
  }

  @Override
  public void initPasswordRecoveryRequest(EmailAddress emailAddress)
      throws SecurityServiceException, ServiceException {
    User user = getByEmailAddressCaseInsensitive(emailAddress);

    if (user != null && user.isEnabled() && user.isNotificationEnabled()) {
      securityManagementService.initiatePasswordRecoveryRequest(
          user,
          hasPassword(user)
              ? UserPasswordRecoveryRequestType.RESET
              : UserPasswordRecoveryRequestType.CREATION,
          UserPasswordRecoveryRequestInitiator.USER);
    }
  }

  @Override
  public void enable(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(!user.isEnabled());
    user.setEnabled(true);
    update(user);
    historyLogService.log(
        HistoryLogEventType.ENABLE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void disable(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(user.isEnabled());
    user.setEnabled(false);
    update(user);
    historyLogService.log(
        HistoryLogEventType.DISABLE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void updateLastLoginDate(User user) throws ServiceException, SecurityServiceException {
    user.setLastLoginDate(Instant.now());
    updateEntity(user);
  }

  @Override
  public void updateLocale(User user, Locale locale)
      throws ServiceException, SecurityServiceException {
    user.setLocale(locale);
    updateEntity(user);
  }

  @Override
  public void updateRoles(User user) throws SecurityServiceException, ServiceException {
    Objects.requireNonNull(user);
    update(user);
  }

  @Override
  public void openAnnouncement(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    user.getAnnouncementInformation().setLastActionDate(Instant.now());
    user.getAnnouncementInformation().setOpen(true);
    update(user);
  }

  @Override
  public void closeAnnouncement(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    user.getAnnouncementInformation().setLastActionDate(Instant.now());
    user.getAnnouncementInformation().setOpen(false);
    update(user);
  }

  @Override
  public User getByUsername(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return getByNaturalId(username);
  }

  @Override
  public User getByUsernameCaseInsensitive(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return dao.getByUsernameCaseInsensitive(username);
  }

  @Override
  public User getByEmailAddressCaseInsensitive(EmailAddress emailAddress) {
    if (emailAddress == null || !StringUtils.hasText(emailAddress.getValue())) {
      return null;
    }
    return dao.getByEmailCaseInsensitive(emailAddress);
  }

  @Override
  public User getAuthenticatedUser() {
    return Optional.ofNullable(authenticationService.getUsername())
        .map(username -> HibernateUtils.unwrap(getByUsername(username)))
        .orElse(null);
  }
}
