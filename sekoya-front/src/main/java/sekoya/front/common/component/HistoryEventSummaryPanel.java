package sekoya.front.common.component;

import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import sekoya.front.common.renderer.HistoryEventSummaryRenderer;

public class HistoryEventSummaryPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public HistoryEventSummaryPanel(
      String id,
      IModel<HistoryEventSummary> creationModel,
      IModel<HistoryEventSummary> modificationModel) {
    super(id);

    add(
        new EnclosureContainer("creation")
            .condition(Condition.modelNotNull(creationModel))
            .add(
                new AttributeAppender(
                    "title",
                    new StringResourceModel(
                        "common.historyEventSummary.value.creation",
                        HistoryEventSummaryRenderer.complet().asModel(creationModel)))),
        new EnclosureContainer("modification")
            .condition(Condition.modelNotNull(modificationModel))
            .add(
                new AttributeAppender(
                    "title",
                    new StringResourceModel(
                        "common.historyEventSummary.value.modification",
                        HistoryEventSummaryRenderer.complet().asModel(modificationModel)))));
  }
}
