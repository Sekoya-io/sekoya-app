package sekoya.front.simulation.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;

public class SimulationSiteOffcanvasPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SimulationSiteOffcanvasPanel(String id) {
    this(id, new GenericEntityModel<>());
  }

  public SimulationSiteOffcanvasPanel(String id, IModel<Site> siteModel) {
    super(id, siteModel);
    setOutputMarkupId(true);

    add(
        new WebMarkupContainer("offcanvas")
            .add(new CoreLabel("nom", BindingModel.of(siteModel, Bindings.site().nom())))
            .setMarkupId("offcanvas-simulation-site"));
  }

  public void setUp(Site site) {
    getModel().setObject(site);
  }
}
