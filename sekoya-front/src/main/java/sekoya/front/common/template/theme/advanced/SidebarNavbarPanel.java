package sekoya.front.common.template.theme.advanced;

import org.apache.wicket.markup.html.panel.Panel;
import sekoya.front.SekoyaApplication;

public class SidebarNavbarPanel extends Panel {

  private static final long serialVersionUID = -3741272240940846720L;

  public SidebarNavbarPanel(String id) {
    super(id);

    add(SekoyaApplication.get().getHomePageLinkDescriptor().link("home"));
  }
}
