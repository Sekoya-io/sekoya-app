package sekoya.front.common.listener;

import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;

public final class BindableModelReadAllListener extends SerializableListener {

  private static final long serialVersionUID = 1L;

  private final IBindableModel<?> bindableModel;

  public static BindableModelReadAllListener of(IBindableModel<?> bindableModel) {
    return new BindableModelReadAllListener(bindableModel);
  }

  private BindableModelReadAllListener(IBindableModel<?> bindableModel) {
    super();
    this.bindableModel = bindableModel;
  }

  @Override
  public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
    bindableModel.readAll();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BindableModelReadAllListener)) {
      return false;
    }
    BindableModelReadAllListener other = (BindableModelReadAllListener) obj;
    return new EqualsBuilder().append(bindableModel, other.bindableModel).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(bindableModel).build();
  }
}
