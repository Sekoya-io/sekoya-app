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
import sekoya.back.business.processus.model.atomic.ProcessusPriorite;

public class ProcessusPrioriteRatingFormCheckValuesPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public ProcessusPrioriteRatingFormCheckValuesPanel(String id) {
    super(id);

    add(
        new CollectionView<>(
            "values",
            new ListModel<>(List.of(ProcessusPriorite.values())),
            Models.serializableModelFactory()) {
          @Override
          protected void populateItem(Item<ProcessusPriorite> item) {
            Radio<ProcessusPriorite> valueFormComponent = new Radio<>("value", item.getModel());

            item.add(
                valueFormComponent,
                new CoreLabel("label", item.getModel().map(ProcessusPriorite::getScore))
                    .add(AttributeModifier.replace("for", valueFormComponent::getMarkupId))
                    .add(
                        AttributeModifier.replace(
                            "title", EnumRenderer.get().asModel(item.getModel())))
                    .add(
                        AttributeModifier.replace(
                            "data-val", item.getModel().map(ProcessusPriorite::getScore))));
          }
        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()));
  }
}
