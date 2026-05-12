package sekoya.back.config;

import igloo.difference.DifferenceIntrospector;
import igloo.difference.DifferenceIntrospectorDefaults;
import igloo.difference.model.DifferenceFields;
import java.util.Set;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sekoya.back.business.common.model.EmailAddress;
import sekoya.back.business.common.model.PhoneNumber;
import sekoya.back.business.common.model.PostalCode;
import sekoya.back.business.role.model.RoleBinding;
import sekoya.back.business.user.difference.service.IUserDifferenceService;
import sekoya.back.business.user.difference.service.UserDifferenceServiceImpl;
import sekoya.back.business.user.model.UserBinding;
import sekoya.back.util.binding.Bindings;

@Configuration
public class SekoyaBackDifferenceConfiguration {

  private static final Set<Class<?>> ADDITIONAL_SIMPLE_TYPES =
      Set.of(EmailAddress.class, PhoneNumber.class, PostalCode.class);

  @Bean
  public IUserDifferenceService userDifferenceService() {
    return new UserDifferenceServiceImpl(userFields());
  }

  public static DifferenceFields userFields() {
    DifferenceIntrospector differenceIntrospector =
        new DifferenceIntrospector(new UserBinding(), ADDITIONAL_SIMPLE_TYPES);

    DifferenceIntrospectorDefaults.ignoreCommonFields(differenceIntrospector);
    differenceIntrospector.addIgnoredPaths(FieldPath.fromString(".toStringHelper"));
    differenceIntrospector.addIgnoredPaths(Bindings.user().type());
    differenceIntrospector.addIgnoredPaths(Bindings.user().fullName());
    differenceIntrospector.addIgnoredPaths(Bindings.user().notificationEmailAddress());
    differenceIntrospector.addIgnoredPaths(Bindings.user().notificationEnabled());
    differenceIntrospector.addIgnoredPaths(Bindings.user().passwordHash());
    differenceIntrospector.addIgnoredPaths(Bindings.user().passwordInformation());
    differenceIntrospector.addIgnoredPaths(Bindings.user().passwordRecoveryRequest());
    differenceIntrospector.addIgnoredPaths(Bindings.user().announcementInformation());
    differenceIntrospector.addIgnoredPaths(Bindings.user().creation());
    differenceIntrospector.addIgnoredPaths(Bindings.user().modification());
    differenceIntrospector.addIgnoredPaths(Bindings.user().lastLoginDate());
    differenceIntrospector.addIgnoredPaths(Bindings.user().locale());

    differenceIntrospector.addBinding(Bindings.user().roles(), new RoleBinding());
    differenceIntrospector.addShallowPaths(Bindings.user().roles());

    return differenceIntrospector.visitBinding();
  }
}
