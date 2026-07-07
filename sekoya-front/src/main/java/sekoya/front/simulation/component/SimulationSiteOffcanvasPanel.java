package sekoya.front.simulation.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.site.page.SiteDetailPage;

public class SimulationSiteOffcanvasPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SimulationSiteOffcanvasPanel(
      String id, IModel<SimulationSearchDto> simulationSearchDtoModel) {
    this(id, new GenericEntityModel<>(), simulationSearchDtoModel);
  }

  public SimulationSiteOffcanvasPanel(
      String id, IModel<Site> siteModel, IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);
    setOutputMarkupId(true);

    add(
        new WebMarkupContainer("offcanvas")
            .add(
                SiteDetailPage.MAPPER
                    .map(siteModel)
                    .link("link")
                    .add(new CoreLabel("nom", BindingModel.of(siteModel, Bindings.site().nom()))),
                new SimulationSiteOffcanvasProcessusPanel("processus", siteModel)
                    .add(
                        Condition.isTrue(
                                BindingModel.of(
                                    simulationSearchDtoModel,
                                    Bindings.simulationSearchDto().applyProcessus()))
                            .thenShow()),
                new SimulationSiteOffcanvasAleaGeographiquePanel(
                    "aleaGeographique", siteModel, simulationSearchDtoModel))
            .setMarkupId("offcanvas-simulation-site"));
  }

  public void onShow(AjaxRequestTarget target, Site site) {
    setModelObject(site);

    target.add(SimulationSiteOffcanvasPanel.this);
    target.appendJavaScript(
        """
        bootstrap.Offcanvas
            .getOrCreateInstance(document.getElementById('offcanvas-simulation-site'))
            .show();
        """);
  }
}
