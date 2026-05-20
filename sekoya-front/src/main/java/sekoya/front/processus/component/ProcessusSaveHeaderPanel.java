package sekoya.front.processus.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import sekoya.back.util.binding.Bindings;
import sekoya.front.processus.model.ProcessusBindableModel;

public class ProcessusSaveHeaderPanel extends AbstractProcessusSavePanel {

  private static final long serialVersionUID = 1L;

  public ProcessusSaveHeaderPanel(String id, ProcessusBindableModel processusBindableModel) {
    super(id, processusBindableModel);

    add(
        new EnclosureContainer("add").condition(addCondition()),
        new EnclosureContainer("edit")
            .condition(addCondition().negate())
            .add(new CoreLabel("nom", processusBindableModel.bind(Bindings.processus().nom()))));
  }
}
