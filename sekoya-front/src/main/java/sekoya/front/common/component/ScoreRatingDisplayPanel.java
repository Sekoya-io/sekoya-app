package sekoya.front.common.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Detachables;
import igloo.wicket.model.Models;
import java.util.Arrays;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.common.model.atomic.IScore;

public class ScoreRatingDisplayPanel<E extends Enum<E> & IScore> extends GenericPanel<E> {

  private static final long serialVersionUID = 1L;

  private final Model<Boolean> smallClassModel = Model.of(Boolean.FALSE);

  public ScoreRatingDisplayPanel(String id, IModel<E> model) {
    super(id, model);

    add(
        new WebMarkupContainer("ratingContainer")
            .add(
                new CollectionView<>(
                    "values",
                    LoadableDetachableModel.of(
                        () ->
                            Arrays.asList(
                                model.getObject().getDeclaringClass().getEnumConstants())),
                    Models.serializableModelFactory()) {
                  @Override
                  protected void populateItem(Item<E> item) {
                    Condition selectedCondition = Condition.isEqual(item.getModel(), model);

                    item.add(new CoreLabel("score", item.getModel().map(E::getScore)))
                        .add(
                            AttributeModifier.replace(
                                "title", EnumRenderer.get().asModel(item.getModel())),
                            AttributeModifier.append(
                                "class",
                                selectedCondition.then("rating-badge-active").otherwise("")),
                            AttributeModifier.replace(
                                "data-val",
                                selectedCondition
                                    .then(item.getModel().map(E::getScore))
                                    .otherwise(Model.of())));
                  }
                }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()))
            .add(
                AttributeModifier.append("class", () -> model.getObject().getRatingCssClass()),
                AttributeModifier.append(
                    "class",
                    Condition.isTrue(smallClassModel)
                        .then("rating-badge-display-sm")
                        .otherwise(""))));

    add(Condition.modelNotNull(model).thenShowInternal());
  }

  public ScoreRatingDisplayPanel<E> small() {
    smallClassModel.setObject(Boolean.TRUE);
    return this;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(smallClassModel);
  }
}
