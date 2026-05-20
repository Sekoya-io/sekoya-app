package sekoya.front.processus.component;

import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.ajax.AjaxRequestTarget.IListener;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.listener.BindableModelReadAllListener;
import sekoya.front.common.listener.BindableModelWriteAllListener;
import sekoya.front.processus.model.ProcessusBindableModel;

public abstract class AbstractProcessusSavePanel extends GenericPanel<Processus> {

  private static final long serialVersionUID = 1L;

  protected final ProcessusBindableModel processusBindableModel;

  private final BindableModelWriteAllListener bindableModelWriteAllListener;

  private final BindableModelReadAllListener bindableModelReadAllListener;

  protected AbstractProcessusSavePanel(String id, ProcessusBindableModel processusBindableModel) {
    super(id, processusBindableModel);
    this.processusBindableModel = processusBindableModel;

    bindableModelWriteAllListener = BindableModelWriteAllListener.of(processusBindableModel);
    bindableModelReadAllListener = BindableModelReadAllListener.of(processusBindableModel);
  }

  protected IListener writeAll() {
    return bindableModelWriteAllListener;
  }

  protected IListener readAll() {
    return bindableModelReadAllListener;
  }

  protected Condition addCondition() {
    return Condition.isTrue(processusBindableModel.bind(Bindings.processus().isNew()));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(processusBindableModel);
  }
}
