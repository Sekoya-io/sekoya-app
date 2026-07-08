package sekoya.front.simulation.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;
import sekoya.front.site.page.SiteDetailPage;

public class SimulationSiteOffcanvasPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  private final IModel<Processus> processusModel = new GenericEntityModel<>();
  private final IModel<Alea> aleaModel = new GenericEntityModel<>();
  private final IModel<Boolean> impactPotentielBrutModeModel = Model.of();

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
                    .link("siteLink")
                    .add(new CoreLabel("site", siteModel).showPlaceholder()),
                new SimulationSiteOffcanvasBreadcrumbPanel(
                    "breadcrumb",
                    siteModel,
                    processusModel,
                    aleaModel,
                    impactPotentielBrutModeModel),
                new SimulationSiteOffcanvasContentPanel(
                    "content",
                    siteModel,
                    processusModel,
                    aleaModel,
                    impactPotentielBrutModeModel,
                    simulationSearchDtoModel))
            .setMarkupId("offcanvas-simulation-site"));
  }

  public void onShow(AjaxRequestTarget target, Site site) {
    setModelObject(site);
    processusModel.setObject(null);
    aleaModel.setObject(null);
    impactPotentielBrutModeModel.setObject(null);

    target.add(SimulationSiteOffcanvasPanel.this);
    target.appendJavaScript(
        """
        bootstrap.Offcanvas
            .getOrCreateInstance(document.getElementById('offcanvas-simulation-site'))
            .show();
        """);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(processusModel, aleaModel);
  }
}
