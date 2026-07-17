package sekoya.front.simulation.component;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.service.business.ISimulationCalculService;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.component.ScoreMonoValueRatingDisplayPanel;
import sekoya.front.processus.page.ProcessusSiteAddPage;

public class SimulationSiteOffcanvasSitePanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ISimulationCalculService simulationCalculService;

  public SimulationSiteOffcanvasSitePanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);

    add(
        new EnclosureContainer("processusContainer")
            .condition(
                Condition.isTrue(
                    BindingModel.of(
                        simulationSearchDtoModel,
                        Bindings.simulationSearchDto().enableProcessus())))
            .add(
                new CollectionView<>(
                    "processus",
                    BindingModel.of(siteModel, Bindings.site().processusEnabled()),
                    GenericEntityModel.factory()) {

                  @Override
                  protected void populateItem(Item<Processus> item) {
                    IModel<Risque> risqueModel =
                        LoadableDetachableModel.of(
                            () ->
                                simulationCalculService.getProcessusRisqueBrut(
                                    item.getModelObject(), simulationSearchDtoModel.getObject()));

                    item.add(
                            new WebMarkupContainer("icon")
                                .add(
                                    new ClassAttributeAppender(
                                        BindingModel.of(
                                            item.getModel(),
                                            Bindings.processus().thematique().iconCssClass()))),
                            new CoreLabel("processus", item.getModel()).showPlaceholder(),
                            new CoreLabel(
                                    "thematique",
                                    BindingModel.of(
                                        item.getModel(), Bindings.processus().type().thematique()))
                                .showPlaceholder(),
                            new ScoreMonoValueRatingDisplayPanel<>("risque", risqueModel))
                        .add(
                            new AjaxEventBehavior("click") {
                              @Override
                              protected void onEvent(AjaxRequestTarget target) {
                                processusModel.setObject(item.getModelObject());
                                target.addChildren(
                                    getPage(), SimulationSiteOffcanvasBreadcrumbPanel.class);
                                target.addChildren(
                                    getPage(), SimulationSiteOffcanvasContentPanel.class);
                              }
                            });
                  }
                }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance())
                    .add(
                        Condition.collectionModelNotEmpty(
                                BindingModel.of(siteModel, Bindings.site().processusEnabled()))
                            .thenShow()),
                new PlaceholderContainer("placeholder")
                    .condition(
                        Condition.collectionModelNotEmpty(
                            BindingModel.of(siteModel, Bindings.site().processusEnabled())))
                    .add(ProcessusSiteAddPage.MAPPER.map(siteModel).link("add"))));

    add(
        new SimulationSiteOffcanvasSiteAleasGeographiquesPanel(
            "aleasGeographiques", siteModel, simulationSearchDtoModel));

    add(Condition.modelNotNull(processusModel).negate().thenShowInternal());
  }
}
