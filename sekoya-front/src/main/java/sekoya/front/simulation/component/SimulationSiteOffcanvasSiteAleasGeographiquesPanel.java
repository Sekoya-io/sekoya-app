package sekoya.front.simulation.component;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Models;
import java.util.SortedSet;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.javatuples.Pair;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.simulation.dto.SimulationParametresDto;
import sekoya.back.business.simulation.service.controller.ISimulationCalculControllerService;
import sekoya.back.business.site.model.Site;
import sekoya.front.common.component.ScoreMonoValueRatingDisplayPanel;

public class SimulationSiteOffcanvasSiteAleasGeographiquesPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ISimulationCalculControllerService simulationCalculControllerService;

  public SimulationSiteOffcanvasSiteAleasGeographiquesPanel(
      String id,
      IModel<Site> siteModel,
      IModel<SimulationParametresDto> simulationParametresDtoModel) {
    super(id, siteModel);

    add(Condition.modelNotNull(siteModel).thenShow());

    IModel<SortedSet<Pair<AleaType, Risque>>> aleasRisquesModel =
        LoadableDetachableModel.of(
            () ->
                simulationCalculControllerService.listAleaRisqueGeographiqueBySite(
                    siteModel.getObject(), simulationParametresDtoModel.getObject()));

    add(
        new CollectionView<>("values", aleasRisquesModel, Models.serializableModelFactory()) {
          @Override
          protected void populateItem(Item<Pair<AleaType, Risque>> item) {

            item.add(
                new WebMarkupContainer("icon")
                    .add(
                        new ClassAttributeAppender(
                            item.getModel().map(Pair::getValue0).map(AleaType::getIconCssClass))),
                new CoreLabel("aleaType", item.getModel().map(Pair::getValue0)),
                new ScoreMonoValueRatingDisplayPanel<Risque>(
                        "risque", item.getModel().map(Pair::getValue1))
                    .small());
          }
        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()));
  }
}
