package sekoya.front.common.form;

import org.iglooproject.wicket.more.bindable.form.CacheWritingForm;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;

public class BindableModelForm<T> extends CacheWritingForm<T> {

  private static final long serialVersionUID = 1L;

  public BindableModelForm(
      String id, IBindableModel<T> mainRootModel, IBindableModel<?>... otherRootModels) {
    super(id, mainRootModel, otherRootModels);
  }

  /*
   * Bypass {@link Form#updateFormComponentModels()} call.
   * This method should not be called when a validation error occurs in one of the form components.
   * Otherwise, raw input will is cleared while model has not be updated.
   */
  @Override
  protected void onError() {
    writeAll();
  }
}
