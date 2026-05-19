package sekoya.front;

import static sekoya.back.property.SekoyaBackPropertyIds.ENVIRONMENT;

import igloo.wicket.model.Detachables;
import java.util.Locale;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.organisation.service.business.IOrganisationService;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.predicate.UserPredicates;
import sekoya.back.business.user.service.business.IUserService;
import sekoya.back.util.Environment;

public class SekoyaSession extends AbstractCoreSession<User> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(SekoyaSession.class);

  @SpringBean private IUserService userService;

  @SpringBean private IOrganisationService organisationService;

  private final IModel<Environment> environmentModel = ApplicationPropertyModel.of(ENVIRONMENT);

  private final IModel<Organisation> organisationModel =
      new SessionThreadSafeGenericEntityModel<>();

  public SekoyaSession(Request request) {
    super(request);
  }

  public static SekoyaSession get() {
    return (SekoyaSession) Session.get();
  }

  @Override
  protected void onSignIn(User user) {
    if (UserPredicates.organisation().apply(user)) {
      organisationModel.setObject(user.getUserOrganisation().getOrganisation());
    } else {
      organisationModel.setObject(organisationService.getDefault());
    }

    try {
      userService.updateLastLoginDate(user);

      Locale locale = user.getLocale();
      if (locale != null) {
        setLocale(user.getLocale());
      } else {
        userService.updateLocale(user, getLocale());
      }
    } catch (Exception e) {
      LOGGER.error(
          String.format("Unable to update the user information on sign in: %1$s", user), e);
    }
  }

  public IModel<Environment> getEnvironmentModel() {
    return environmentModel;
  }

  public IModel<Organisation> getOrganisationModel() {
    return organisationModel;
  }

  public boolean hasOrganisation() {
    return organisationModel.getObject() != null;
  }

  @Override
  public void detach() {
    super.detach();
    Detachables.detach(environmentModel, organisationModel);
  }
}
