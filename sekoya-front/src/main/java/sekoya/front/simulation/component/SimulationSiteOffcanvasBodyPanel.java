package sekoya.front.simulation.component;

import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.model.atomic.SimulationEtape;
import sekoya.back.business.site.model.Site;

public class SimulationSiteOffcanvasBodyPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SimulationSiteOffcanvasBodyPanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<Alea> aleaModel,
      IModel<SimulationEtape> simulationEtapeModel,
      IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);
    setOutputMarkupPlaceholderTag(true);

    add(
        new SimulationSiteOffcanvasBreadcrumbPanel(
            "breadcrumb", siteModel, processusModel, aleaModel, simulationEtapeModel));

    add(
        new SimulationSiteOffcanvasSitePanel(
            "site", siteModel, processusModel, simulationEtapeModel, simulationSearchDtoModel),
        new SimulationSiteOffcanvasProcessusPanel(
            "processus",
            siteModel,
            processusModel,
            aleaModel,
            simulationEtapeModel,
            simulationSearchDtoModel),
        new SimulationSiteOffcanvasAleaPanel(
            "alea",
            siteModel,
            processusModel,
            aleaModel,
            simulationEtapeModel,
            simulationSearchDtoModel),
        new SimulationSiteOffcanvasImpactPotentielBrutPanel(
            "impactPotentielBrut",
            siteModel,
            processusModel,
            aleaModel,
            simulationEtapeModel,
            simulationSearchDtoModel));

    add(Condition.modelNotNull(siteModel).thenShowInternal());
  }
}
