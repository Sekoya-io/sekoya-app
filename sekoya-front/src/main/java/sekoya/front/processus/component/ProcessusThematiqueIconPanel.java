package sekoya.front.processus.component;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.processus.model.atomic.ProcessusThematique;
import sekoya.back.util.binding.Bindings;

public class ProcessusThematiqueIconPanel extends GenericPanel<ProcessusThematique> {

  private static final long serialVersionUID = 1L;

  public ProcessusThematiqueIconPanel(
      String id, IModel<ProcessusThematique> processusThematiqueModel) {
    super(id, processusThematiqueModel);

    add(
        new WebMarkupContainer("icon")
            .add(
                new AttributeModifier(
                    "title", EnumRenderer.get().asModel(processusThematiqueModel)),
                new ClassAttributeAppender(
                    BindingModel.of(
                        processusThematiqueModel, Bindings.processusThematique().iconCssClass()))),
        new CoreLabel("thematique", processusThematiqueModel).showPlaceholder());
  }
}
