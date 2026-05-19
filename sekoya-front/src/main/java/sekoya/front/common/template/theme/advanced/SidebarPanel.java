package sekoya.front.common.template.theme.advanced;

import java.util.List;
import java.util.function.Supplier;
import org.apache.wicket.markup.html.WebPage;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import sekoya.front.common.component.ApplicationEnvironmentPanel;
import sekoya.front.common.template.theme.common.AbstractNavbarPanel;

public class SidebarPanel extends AbstractNavbarPanel {

  private static final long serialVersionUID = 3187936834300791175L;

  public SidebarPanel(
      String id,
      Supplier<List<NavigationMenuItem>> mainNavSupplier,
      Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
      Supplier<Class<? extends WebPage>> secondMenuPageSupplier) {
    super(id);
    setMarkupId(id);

    add(new SidebarNavbarPanel("navbar"), new ApplicationEnvironmentPanel("environment"));

    add(
        new SidebarHeaderPanel("header"),
        new SidebarUserPanel("user"),
        new SidebarOrganisationPanel("quickSearch"),
        new SidebarMenuPanel(
            "menu", mainNavSupplier, firstMenuPageSupplier, secondMenuPageSupplier));

    add(new SidebarFooterPanel("footer"));
  }
}
