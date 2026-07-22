package sekoya.front.simulation.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationParametresDto;
import sekoya.back.business.simulation.model.atomic.SimulationEtape;
import sekoya.back.business.site.model.Site;

public class SimulationSiteOffcanvasHeaderPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SimulationSiteOffcanvasHeaderPanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<Alea> aleaModel,
      IModel<SimulationEtape> simulationEtapeModel,
      IModel<SimulationParametresDto> simulationParametresDtoModel) {
    super(id, siteModel);
    setOutputMarkupId(true);

    add(new CoreLabel("etape", simulationEtapeModel).showPlaceholder());
  }
}
