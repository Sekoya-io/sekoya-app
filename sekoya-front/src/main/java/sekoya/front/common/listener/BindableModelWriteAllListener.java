package sekoya.front.common.listener;

import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;

public final class BindableModelWriteAllListener extends SerializableListener {

  private static final long serialVersionUID = 1L;

  private final IBindableModel<?> bindableModel;

  public static BindableModelWriteAllListener of(IBindableModel<?> bindableModel) {
    return new BindableModelWriteAllListener(bindableModel);
  }

  private BindableModelWriteAllListener(IBindableModel<?> bindableModel) {
    super();
    this.bindableModel = bindableModel;
  }

  @Override
  public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
    bindableModel.writeAll();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BindableModelWriteAllListener)) {
      return false;
    }
    BindableModelWriteAllListener other = (BindableModelWriteAllListener) obj;
    return new EqualsBuilder().append(bindableModel, other.bindableModel).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(bindableModel).build();
  }
}
