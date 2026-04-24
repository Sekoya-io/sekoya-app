package sekoya.front.common.template.theme.common;

import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import org.apache.wicket.model.Model;
import sekoya.back.util.Environment;
import sekoya.front.SekoyaSession;

public class BootstrapBreakpointPanel extends EnclosureContainer {

  private static final long serialVersionUID = 5271828582493462504L;

  public BootstrapBreakpointPanel(String id) {
    super(id);
    setOutputMarkupId(true);

    condition(
        Condition.isEqual(
            SekoyaSession.get().getEnvironmentModel(), Model.of(Environment.development)));
  }
}
