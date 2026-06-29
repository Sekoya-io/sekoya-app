package sekoya.front.simulation.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Models;
import java.util.SortedSet;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.javatuples.Pair;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.service.controller.ISiteControllerService;
import sekoya.front.common.component.RisqueRatingDisplayPanel;

public class SimulationSiteOffcanvasAleaGeographiquePanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ISiteControllerService siteControllerService;

  public SimulationSiteOffcanvasAleaGeographiquePanel(
      String id, IModel<Site> siteModel, IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);

    add(Condition.modelNotNull(siteModel).thenShow());

    IModel<SortedSet<Pair<AleaType, Risque>>> aleasRisquesModel =
        LoadableDetachableModel.of(
            () ->
                siteControllerService.listAleaRisqueGeographique(
                    siteModel.getObject(), simulationSearchDtoModel.getObject()));

    add(
        new CollectionView<>("values", aleasRisquesModel, Models.serializableModelFactory()) {
          @Override
          protected void populateItem(Item<Pair<AleaType, Risque>> item) {

            item.add(
                new CoreLabel("aleaType", Model.of(item.getModelObject().getValue0())),
                new RisqueRatingDisplayPanel("risque", Model.of(item.getModelObject().getValue1()))
                    .small());
          }
        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()));
  }
}
