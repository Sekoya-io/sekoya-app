package sekoya.front.processus.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Models;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.alea.model.atomic.AleaSensibilite;

public class AleaSensibiliteRatingDisplayPanel extends GenericPanel<AleaSensibilite> {

  private static final long serialVersionUID = 1L;

  public AleaSensibiliteRatingDisplayPanel(
      String id, IModel<AleaSensibilite> aleaSensibiliteModel) {
    super(id, aleaSensibiliteModel);

    add(
        new CollectionView<>(
            "values",
            new ListModel<>(List.of(AleaSensibilite.values())),
            Models.serializableModelFactory()) {
          @Override
          protected void populateItem(Item<AleaSensibilite> item) {
            Condition selectedCondition = Condition.isEqual(item.getModel(), aleaSensibiliteModel);

            item.add(new CoreLabel("score", item.getModel().map(AleaSensibilite::getScore)))
                .add(
                    AttributeModifier.replace("title", EnumRenderer.get().asModel(item.getModel())))
                .add(
                    AttributeModifier.append(
                        "class", selectedCondition.then("rating-badge-active").otherwise("")))
                .add(
                    AttributeModifier.replace(
                        "data-val",
                        selectedCondition
                            .then(item.getModel().map(AleaSensibilite::getScore))
                            .otherwise(Model.of())));
          }
        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()));
  }
}
