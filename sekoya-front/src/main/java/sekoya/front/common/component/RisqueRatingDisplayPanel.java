package sekoya.front.common.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Detachables;
import igloo.wicket.model.Models;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.common.model.atomic.Risque;

public class RisqueRatingDisplayPanel extends GenericPanel<Risque> {

  private static final long serialVersionUID = 1L;

  private Model<Boolean> smallClassModel = Model.of(Boolean.FALSE);

  public RisqueRatingDisplayPanel(String id, IModel<Risque> risqueModel) {
    super(id, risqueModel);

    add(
        new WebMarkupContainer("ratingContainer")
            .add(
                new CollectionView<>(
                    "values",
                    new ListModel<>(List.of(Risque.values())),
                    Models.serializableModelFactory()) {
                  @Override
                  protected void populateItem(Item<Risque> item) {
                    Condition selectedCondition = Condition.isEqual(item.getModel(), risqueModel);

                    item.add(new CoreLabel("score", item.getModel().map(Risque::getScore)))
                        .add(
                            AttributeModifier.replace(
                                "title", EnumRenderer.get().asModel(item.getModel())))
                        .add(
                            AttributeModifier.append(
                                "class",
                                selectedCondition.then("rating-badge-active").otherwise("")))
                        .add(
                            AttributeModifier.replace(
                                "data-val",
                                selectedCondition
                                    .then(item.getModel().map(Risque::getScore))
                                    .otherwise(Model.of())));
                  }
                }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()))
            .add(
                AttributeModifier.append(
                    "class",
                    Condition.isTrue(smallClassModel)
                        .then("rating-badge-display-sm")
                        .otherwise(""))));
  }

  public RisqueRatingDisplayPanel small() {
    smallClassModel.setObject(Boolean.TRUE);
    return this;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(smallClassModel);
  }
}
