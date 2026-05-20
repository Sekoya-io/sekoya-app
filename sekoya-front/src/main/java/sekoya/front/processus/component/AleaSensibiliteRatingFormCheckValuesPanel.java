package sekoya.front.processus.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.Models;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.util.ListModel;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.alea.model.atomic.AleaSensibilite;

public class AleaSensibiliteRatingFormCheckValuesPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public AleaSensibiliteRatingFormCheckValuesPanel(String id) {
    super(id);

    add(
        new CollectionView<>(
            "values",
            new ListModel<>(List.of(AleaSensibilite.values())),
            Models.serializableModelFactory()) {
          @Override
          protected void populateItem(Item<AleaSensibilite> item) {
            Radio<AleaSensibilite> valueFormComponent = new Radio<>("value", item.getModel());

            item.add(
                valueFormComponent,
                new CoreLabel("label", item.getModel().map(AleaSensibilite::getScore))
                    .add(AttributeModifier.replace("for", valueFormComponent::getMarkupId))
                    .add(
                        AttributeModifier.replace(
                            "title", EnumRenderer.get().asModel(item.getModel())))
                    .add(
                        AttributeModifier.replace(
                            "data-val", item.getModel().map(AleaSensibilite::getScore))));
          }
        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()));
  }
}
