package sekoya.front.simulation.component;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.site.model.Site;

public class SimulationSiteOffcanvasProcessusPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SimulationSiteOffcanvasProcessusPanel(String id, IModel<Site> siteModel) {
    super(id, siteModel);
  }
}
