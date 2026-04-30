package sekoya.front.referencedata.popup;

import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.ResourceModel;
import sekoya.back.business.referencedata.model.ReferenceData;
import sekoya.back.util.binding.Bindings;

public class BasicReferenceDataPopup<T extends ReferenceData<? super T>>
    extends AbstractReferenceDataPopup<T> {

  private static final long serialVersionUID = -729754666642757497L;

  private TextField<String> label;
  private CheckBox enabled;

  public BasicReferenceDataPopup(String id) {
    super(id);
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    form = new Form<>("form", getModel());
    body.add(form);

    this.label =
        new TextField<>(
            "label", BindingModel.of(getModel(), Bindings.referenceData().label()), String.class);
    this.enabled =
        new CheckBox("enabled", BindingModel.of(getModel(), Bindings.referenceData().enabled()));

    form.add(
        label.setLabel(new ResourceModel("business.referenceData.label")).setRequired(true),
        enabled
            .setLabel(new ResourceModel("business.referenceData.enabled"))
            .add(
                Condition.isTrue(
                        BindingModel.of(getModel(), Bindings.referenceData().disableable()))
                    .thenEnable())
            .setOutputMarkupId(true));

    return body;
  }

  protected final TextField<String> getLabel() {
    return label;
  }

  protected final CheckBox getEnabled() {
    return enabled;
  }

  @Override
  protected void refresh(AjaxRequestTarget target) {
    // nothing to do
  }
}
