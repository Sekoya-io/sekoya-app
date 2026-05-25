package sekoya.front.processus.component;

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
import sekoya.back.business.processus.model.atomic.ProcessusPriorite;

public class ProcessusPrioriteRatingDisplayPanel extends GenericPanel<ProcessusPriorite> {

  private static final long serialVersionUID = 1L;

  private Model<Boolean> smallClassModel = Model.of(Boolean.FALSE);

  public ProcessusPrioriteRatingDisplayPanel(
      String id, IModel<ProcessusPriorite> processusPrioriteModel) {
    super(id, processusPrioriteModel);

    add(
        new WebMarkupContainer("ratingContainer")
            .add(
                new CollectionView<>(
                    "values",
                    new ListModel<>(List.of(ProcessusPriorite.values())),
                    Models.serializableModelFactory()) {
                  @Override
                  protected void populateItem(Item<ProcessusPriorite> item) {
                    Condition selectedCondition =
                        Condition.isEqual(item.getModel(), processusPrioriteModel);

                    item.add(
                            new CoreLabel(
                                "score", item.getModel().map(ProcessusPriorite::getScore)))
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
                                    .then(item.getModel().map(ProcessusPriorite::getScore))
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

  public ProcessusPrioriteRatingDisplayPanel small() {
    smallClassModel.setObject(Boolean.TRUE);
    return this;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(smallClassModel);
  }
}
