package sekoya.front.console.common.component;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.model.IModel;
import sekoya.back.util.Environment;
import sekoya.front.SekoyaSession;

public class ConsoleEnvironmentPanel extends GenericPanel<Environment> {

  private static final long serialVersionUID = -1099199206441256170L;

  public ConsoleEnvironmentPanel(String id) {
    this(id, SekoyaSession.get().getEnvironmentModel());
  }

  public ConsoleEnvironmentPanel(String id, IModel<Environment> environmentModel) {
    super(id, environmentModel);
    setOutputMarkupId(true);

    add(new EnumLabel<>("environment", environmentModel));
    add(new ClassAttributeAppender(() -> "environment-section-" + environmentModel.getObject()));
  }
}
