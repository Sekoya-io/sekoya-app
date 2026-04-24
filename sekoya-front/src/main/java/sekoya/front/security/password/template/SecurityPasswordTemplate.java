package sekoya.front.security.password.template;

import igloo.wicket.condition.Condition;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import sekoya.front.SekoyaSession;
import sekoya.front.common.template.ApplicationAccessTemplate;

public abstract class SecurityPasswordTemplate extends ApplicationAccessTemplate {

  private static final long serialVersionUID = -4350860041946569108L;

  protected SecurityPasswordTemplate(PageParameters parameters) {
    super(parameters);

    if (!keepSignedIn().applies() && SekoyaSession.get().isSignedIn()) {
      SekoyaSession.get().invalidate();
      throw new RestartResponseException(getClass(), parameters);
    }
  }

  public Condition keepSignedIn() {
    return Condition.alwaysTrue();
  }
}
