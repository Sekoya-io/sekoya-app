package sekoya.front.common.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.common.model.atomic.IScore;

public class ScoreMonoValueRatingDisplayPanel<E extends Enum<E> & IScore> extends GenericPanel<E> {

  private static final long serialVersionUID = 1L;

  private final Model<Boolean> smallClassModel = Model.of(Boolean.FALSE);

  public ScoreMonoValueRatingDisplayPanel(String id, IModel<E> model) {
    super(id, model);

    add(
        new WebMarkupContainer("ratingContainer")
            .add(
                new CoreLabel("score", model.map(E::getScore))
                    .add(
                        AttributeModifier.replace("title", EnumRenderer.get().asModel(model)),
                        AttributeModifier.replace("data-val", model.map(E::getScore))))
            .add(
                AttributeModifier.append("class", model.getObject().getRatingCssClass()),
                AttributeModifier.append(
                    "class",
                    Condition.isTrue(smallClassModel)
                        .then("rating-badge-display-sm")
                        .otherwise(""))));
  }

  public ScoreMonoValueRatingDisplayPanel<E> small() {
    smallClassModel.setObject(Boolean.TRUE);
    return this;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(smallClassModel);
  }
}
