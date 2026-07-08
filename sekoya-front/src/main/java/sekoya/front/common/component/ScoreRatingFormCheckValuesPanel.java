package sekoya.front.common.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.Models;
import java.util.Arrays;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.util.ListModel;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.common.model.atomic.IScore;

public class ScoreRatingFormCheckValuesPanel<E extends Enum<E> & IScore> extends Panel {

  private static final long serialVersionUID = 1L;

  public ScoreRatingFormCheckValuesPanel(String id, Class<E> clazz) {
    super(id);

    add(
        new CollectionView<>(
            "values",
            new ListModel<>(Arrays.asList(clazz.getEnumConstants())),
            Models.serializableModelFactory()) {
          @Override
          protected void populateItem(Item<E> item) {
            Radio<E> valueFormComponent = new Radio<>("value", item.getModel());

            item.add(
                valueFormComponent,
                new CoreLabel("label", item.getModel().map(E::getScore))
                    .add(AttributeModifier.replace("for", valueFormComponent::getMarkupId))
                    .add(
                        AttributeModifier.replace(
                            "title", EnumRenderer.get().asModel(item.getModel())))
                    .add(AttributeModifier.replace("data-val", item.getModel().map(E::getScore))));
          }
        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()));
  }
}
