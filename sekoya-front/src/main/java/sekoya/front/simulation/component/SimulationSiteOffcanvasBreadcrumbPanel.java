package sekoya.front.simulation.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.component.TargetBlankBehavior;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.model.atomic.SimulationEtape;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.processus.page.ProcessusDetailPage;
import sekoya.front.site.page.SiteDetailPage;

public class SimulationSiteOffcanvasBreadcrumbPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SimulationSiteOffcanvasBreadcrumbPanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<Alea> aleaModel,
      IModel<SimulationEtape> simulationEtapeModel) {
    super(id, siteModel);

    add(
        new WebMarkupContainer("siteContainer")
            .add(
                new AjaxLink<Void>("navigationLink") {

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    processusModel.setObject(null);
                    aleaModel.setObject(null);
                    simulationEtapeModel.setObject(SimulationEtape.SITE);
                    target.addChildren(getPage(), SimulationSiteOffcanvasHeaderPanel.class);
                    target.addChildren(getPage(), SimulationSiteOffcanvasBodyPanel.class);
                  }
                }.add(new CoreLabel("site", siteModel).showPlaceholder())
                    .add(
                        Condition.isTrue(
                                () ->
                                    simulationEtapeModel.getObject().isAfter(SimulationEtape.SITE))
                            .thenShow()),
                new CoreLabel("site", siteModel)
                    .showPlaceholder()
                    .add(
                        Condition.isEqual(simulationEtapeModel, Model.of(SimulationEtape.SITE))
                            .thenShow()),
                SiteDetailPage.MAPPER
                    .map(siteModel)
                    .link("siteLink")
                    .add(new TargetBlankBehavior())),
        new EnclosureContainer("processusContainer")
            .condition(
                Condition.isTrue(
                    () ->
                        simulationEtapeModel
                            .getObject()
                            .isAfterOrCurrent(SimulationEtape.PROCESSUS)))
            .add(
                new AjaxLink<Void>("navigationLink") {

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    aleaModel.setObject(null);
                    simulationEtapeModel.setObject(SimulationEtape.PROCESSUS);
                    target.addChildren(getPage(), SimulationSiteOffcanvasHeaderPanel.class);
                    target.addChildren(getPage(), SimulationSiteOffcanvasBodyPanel.class);
                  }
                }.add(new CoreLabel("processus", processusModel).showPlaceholder())
                    .add(
                        Condition.isTrue(
                                () ->
                                    simulationEtapeModel
                                        .getObject()
                                        .isAfter(SimulationEtape.PROCESSUS))
                            .thenShow()),
                new CoreLabel("processus", processusModel)
                    .showPlaceholder()
                    .add(
                        Condition.isEqual(simulationEtapeModel, Model.of(SimulationEtape.PROCESSUS))
                            .thenShow()),
                ProcessusDetailPage.MAPPER
                    .map(processusModel)
                    .link("processusLink")
                    .add(new TargetBlankBehavior())),
        new EnclosureContainer("aleaContainer")
            .condition(
                Condition.isTrue(
                    () -> simulationEtapeModel.getObject().isAfterOrCurrent(SimulationEtape.ALEA)))
            .add(
                new AjaxLink<Void>("navigationLink") {

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    simulationEtapeModel.setObject(SimulationEtape.ALEA);
                    target.addChildren(getPage(), SimulationSiteOffcanvasHeaderPanel.class);
                    target.addChildren(getPage(), SimulationSiteOffcanvasBodyPanel.class);
                  }
                }.add(new CoreLabel("alea", aleaModel).showPlaceholder())
                    .add(
                        Condition.isTrue(
                                () ->
                                    simulationEtapeModel.getObject().isAfter(SimulationEtape.ALEA))
                            .thenShow()),
                new CoreLabel("alea", aleaModel)
                    .showPlaceholder()
                    .add(
                        Condition.isEqual(simulationEtapeModel, Model.of(SimulationEtape.ALEA))
                            .thenShow())),
        new EnclosureContainer("impactPotentielBrutContainer")
            .condition(
                Condition.isTrue(
                    () ->
                        simulationEtapeModel
                            .getObject()
                            .isAfterOrCurrent(SimulationEtape.IMPACT_POTENTIEL)))
            .add(
                new CoreLabel(
                    "impactPotentielBrut",
                    BindingModel.of(aleaModel, Bindings.alea().impactPotentielBrut()))));

    add(Condition.anyChildVisible(this).thenShowInternal());
  }
}
