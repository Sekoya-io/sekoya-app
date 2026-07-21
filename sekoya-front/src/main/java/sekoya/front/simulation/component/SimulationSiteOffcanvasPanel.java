package sekoya.front.simulation.component;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.model.atomic.SimulationEtape;
import sekoya.back.business.site.model.Site;
import sekoya.front.navigation.page.HomePage;

public class SimulationSiteOffcanvasPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  private final IModel<Processus> processusModel = new GenericEntityModel<>();
  private final IModel<Alea> aleaModel = new GenericEntityModel<>();
  private final IModel<SimulationEtape> simulationEtapeModel = Model.of(SimulationEtape.SITE);

  public SimulationSiteOffcanvasPanel(
      String id, IModel<SimulationSearchDto> simulationSearchDtoModel) {
    this(id, new GenericEntityModel<>(), simulationSearchDtoModel);
  }

  public SimulationSiteOffcanvasPanel(
      String id, IModel<Site> siteModel, IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);
    setOutputMarkupId(true);

    Condition homePageCondition = Condition.of(() -> getPage() instanceof HomePage);

    add(
        new WebMarkupContainer("offcanvas")
            .add(
                new SimulationSiteOffcanvasHeaderPanel(
                    "header",
                    siteModel,
                    processusModel,
                    aleaModel,
                    simulationEtapeModel,
                    simulationSearchDtoModel),
                new SimulationSiteOffcanvasBodyPanel(
                    "body",
                    siteModel,
                    processusModel,
                    aleaModel,
                    simulationEtapeModel,
                    simulationSearchDtoModel))
            .add(
                new ClassAttributeAppender("home-offcanvas home-offcanvas-simulation") {
                  @Override
                  public boolean isEnabled(Component component) {
                    return homePageCondition.applies();
                  }
                },
                new AttributeModifier("data-bs-scroll", "true") {
                  @Override
                  public boolean isEnabled(Component component) {
                    return homePageCondition.applies();
                  }
                },
                new AttributeModifier("data-bs-backdrop", "false") {
                  @Override
                  public boolean isEnabled(Component component) {
                    return homePageCondition.applies();
                  }
                })
            .setMarkupId("offcanvas-simulation-site"));
  }

  public void onShow(AjaxRequestTarget target, Site site) {
    setModelObject(site);
    reset();

    target.add(SimulationSiteOffcanvasPanel.this);
    target.appendJavaScript(
        """
        bootstrap.Offcanvas
            .getOrCreateInstance(document.getElementById('offcanvas-simulation-site'))
            .show();
        """);
  }

  public void reset() {
    processusModel.setObject(null);
    aleaModel.setObject(null);
    simulationEtapeModel.setObject(SimulationEtape.SITE);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(processusModel, aleaModel, simulationEtapeModel);
  }
}
