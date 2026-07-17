package sekoya.front.simulation.component;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;

public class SimulationSiteOffcanvasContentPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SimulationSiteOffcanvasContentPanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<Alea> aleaModel,
      IModel<Boolean> impactPotentielBrutModeModel,
      IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);
    setOutputMarkupId(true);

    add(
        new SimulationSiteOffcanvasBreadcrumbPanel(
            "breadcrumb", siteModel, processusModel, aleaModel, impactPotentielBrutModeModel));

    add(
        new SimulationSiteOffcanvasSitePanel(
            "site", siteModel, processusModel, simulationSearchDtoModel),
        new SimulationSiteOffcanvasProcessusPanel(
            "processus", siteModel, processusModel, aleaModel, simulationSearchDtoModel),
        new SimulationSiteOffcanvasAleaPanel(
            "alea",
            siteModel,
            processusModel,
            aleaModel,
            impactPotentielBrutModeModel,
            simulationSearchDtoModel),
        new SimulationSiteOffcanvasImpactPotentielBrutPanel(
            "impactPotentielBrut",
            siteModel,
            processusModel,
            aleaModel,
            impactPotentielBrutModeModel,
            simulationSearchDtoModel));
  }
}
