package sekoya.front.simulation.component;

import com.google.common.collect.ImmutableList;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Models;
import java.util.Comparator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.javatuples.Pair;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.service.IAleaService;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.service.business.ISimulationCalculService;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.behavior.AjaxClickA11yEventBehavior;
import sekoya.front.common.component.ScoreMonoValueRatingDisplayPanel;
import sekoya.front.processus.page.ProcessusEditPage;

public class SimulationSiteOffcanvasProcessusPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IAleaService aleaService;

  @SpringBean private ISimulationCalculService simulationCalculService;

  public SimulationSiteOffcanvasProcessusPanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<Alea> aleaModel,
      IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);

    var aleasRisqueModel =
        LoadableDetachableModel.of(
            () ->
                processusModel.getObject().getAleas().stream()
                    .map(
                        alea ->
                            Pair.with(
                                alea.getId(),
                                simulationCalculService.getAleaRisqueBrut(
                                    alea, simulationSearchDtoModel.getObject())))
                    .sorted(
                        Comparator.comparingInt((Pair<Long, Risque> p) -> p.getValue1().getScore())
                            .reversed())
                    .collect(ImmutableList.toImmutableList()));

    add(
        new CollectionView<>("aleas", aleasRisqueModel, Models.serializableModelFactory()) {

          @Override
          protected void populateItem(Item<Pair<Long, Risque>> item) {
            IModel<Alea> itemAleaModel =
                LoadableDetachableModel.of(
                    () -> aleaService.getById(item.getModelObject().getValue0()));

            item.add(
                    new WebMarkupContainer("icon")
                        .add(
                            new ClassAttributeAppender(
                                BindingModel.of(
                                    itemAleaModel, Bindings.alea().type().iconCssClass()))),
                    new CoreLabel("alea", itemAleaModel).showPlaceholder(),
                    new ScoreMonoValueRatingDisplayPanel<>(
                            "risque", () -> item.getModelObject().getValue1())
                        .small())
                .add(
                    new AjaxClickA11yEventBehavior() {
                      @Override
                      protected void onEvent(AjaxRequestTarget target) {
                        aleaModel.setObject(itemAleaModel.getObject());
                        target.addChildren(getPage(), SimulationSiteOffcanvasContentPanel.class);
                      }
                    });
          }
        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance())
            .add(Condition.collectionModelNotEmpty(aleasRisqueModel).thenShow()),
        new PlaceholderContainer("placeholder")
            .condition(Condition.collectionModelNotEmpty(aleasRisqueModel))
            .add(ProcessusEditPage.MAPPER.map(processusModel).link("add")));

    add(
        Condition.and(
                Condition.modelNotNull(processusModel), Condition.modelNotNull(aleaModel).negate())
            .thenShowInternal());
  }
}
