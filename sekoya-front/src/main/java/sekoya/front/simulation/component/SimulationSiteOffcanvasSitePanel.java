package sekoya.front.simulation.component;

import com.google.common.collect.ImmutableList;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Models;
import java.util.Comparator;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.javatuples.Pair;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.service.IProcessusService;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.service.business.ISimulationCalculService;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.component.ScoreMonoValueRatingDisplayPanel;
import sekoya.front.processus.page.ProcessusSiteAddPage;

public class SimulationSiteOffcanvasSitePanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IProcessusService processusService;

  @SpringBean private ISimulationCalculService simulationCalculService;

  public SimulationSiteOffcanvasSitePanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);

    var processusRisqueModel =
        LoadableDetachableModel.of(
            () ->
                siteModel.getObject().getProcessusEnabled().stream()
                    .map(
                        processus ->
                            Pair.with(
                                processus.getId(),
                                simulationCalculService.getProcessusRisqueBrut(
                                    processus, simulationSearchDtoModel.getObject())))
                    .sorted(
                        Comparator.comparingInt((Pair<Long, Risque> p) -> p.getValue1().getScore())
                            .reversed())
                    .collect(ImmutableList.toImmutableList()));

    add(
        new EnclosureContainer("processusContainer")
            .condition(
                Condition.isTrue(
                    BindingModel.of(
                        simulationSearchDtoModel,
                        Bindings.simulationSearchDto().enableProcessus())))
            .add(
                new CollectionView<>(
                    "processus", processusRisqueModel, Models.serializableModelFactory()) {

                  @Override
                  protected void populateItem(Item<Pair<Long, Risque>> item) {
                    IModel<Processus> itemProcessusModel =
                        LoadableDetachableModel.of(
                            () -> processusService.getById(item.getModelObject().getValue0()));

                    item.add(
                            new WebMarkupContainer("icon")
                                .add(
                                    new ClassAttributeAppender(
                                        BindingModel.of(
                                            itemProcessusModel,
                                            Bindings.processus().thematique().iconCssClass()))),
                            new CoreLabel("processus", itemProcessusModel).showPlaceholder(),
                            new CoreLabel(
                                    "thematique",
                                    BindingModel.of(
                                        itemProcessusModel,
                                        Bindings.processus().type().thematique()))
                                .showPlaceholder(),
                            new ScoreMonoValueRatingDisplayPanel<>(
                                "risque", () -> item.getModelObject().getValue1()))
                        .add(
                            new AjaxEventBehavior("click") {
                              @Override
                              protected void onEvent(AjaxRequestTarget target) {
                                processusModel.setObject(itemProcessusModel.getObject());
                                target.addChildren(
                                    getPage(), SimulationSiteOffcanvasBreadcrumbPanel.class);
                                target.addChildren(
                                    getPage(), SimulationSiteOffcanvasContentPanel.class);
                              }
                            });
                  }
                }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance())
                    .add(Condition.collectionModelNotEmpty(processusRisqueModel).thenShow()),
                new PlaceholderContainer("placeholder")
                    .condition(Condition.collectionModelNotEmpty(processusRisqueModel))
                    .add(ProcessusSiteAddPage.MAPPER.map(siteModel).link("add"))));

    add(
        new SimulationSiteOffcanvasSiteAleasGeographiquesPanel(
            "aleasGeographiques", siteModel, simulationSearchDtoModel));

    add(
        Condition.and(
                Condition.modelNotNull(siteModel), Condition.modelNotNull(processusModel).negate())
            .thenShowInternal());
  }
}
