package sekoya.front.simulation.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.navigation.page.HomePage;

// TODO : permissions
public abstract class SimulationTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  protected SimulationTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.simulation"), HomePage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return HomePage.class;
  }
}
