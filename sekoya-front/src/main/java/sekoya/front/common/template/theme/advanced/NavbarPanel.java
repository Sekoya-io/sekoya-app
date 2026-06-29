package sekoya.front.common.template.theme.advanced;

import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import java.util.function.Supplier;
import org.apache.wicket.markup.html.WebPage;
import sekoya.front.SekoyaApplication;
import sekoya.front.common.template.theme.common.AbstractNavbarPanel;
import sekoya.front.navigation.page.HomePage;
import sekoya.front.simulation.page.SimulationListPage;
import sekoya.front.simulation.template.SimulationTemplate;

public class NavbarPanel extends AbstractNavbarPanel {

  private static final long serialVersionUID = 3273009208331893767L;

  public NavbarPanel(String id, Supplier<Class<? extends WebPage>> firstMenuPageSupplier) {
    super(id);

    add(SekoyaApplication.get().getHomePageLinkDescriptor().link("home"));

    add(
        new EnclosureContainer("simulationModeContainer")
            .condition(Condition.isTrue(() -> getPage() instanceof SimulationTemplate))
            .add(
                HomePage.linkDescriptor()
                    .link("homePageLink")
                    .add(
                        Condition.isTrue(() -> getPage() instanceof SimulationListPage).thenShow()),
                SimulationListPage.linkDescriptor()
                    .link("simlationListLink")
                    .add(Condition.isTrue(() -> getPage() instanceof HomePage).thenShow())));
  }
}
