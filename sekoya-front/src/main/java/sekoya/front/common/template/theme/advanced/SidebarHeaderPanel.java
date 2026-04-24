package sekoya.front.common.template.theme.advanced;

import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.panel.Panel;
import sekoya.front.SekoyaApplication;

public class SidebarHeaderPanel extends Panel {

  private static final long serialVersionUID = -926306336711136387L;

  public SidebarHeaderPanel(String id) {
    super(id);

    add(Condition.anyChildVisible(this).thenShow());

    add(SekoyaApplication.get().getHomePageLinkDescriptor().link("home"));
  }
}
