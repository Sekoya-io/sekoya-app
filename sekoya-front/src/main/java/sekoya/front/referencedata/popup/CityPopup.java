package sekoya.front.referencedata.popup;

import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import sekoya.back.business.common.model.PostalCode;
import sekoya.back.business.referencedata.model.City;
import sekoya.back.util.binding.Bindings;
import sekoya.front.referencedata.validator.CityUnicityFormValidator;

public abstract class CityPopup extends AbstractReferenceDataPopup<City> {

  private static final long serialVersionUID = -4941198698654382836L;

  public CityPopup(String id) {
    super(id);
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    IModel<City> model = getModel();

    form = new Form<City>("form", model);

    TextField<String> label =
        new TextField<>("label", BindingModel.of(model, Bindings.city().label()));
    TextField<PostalCode> postalCode =
        new TextField<>(
            "postalCode", BindingModel.of(model, Bindings.city().postalCode()), PostalCode.class);

    body.add(
        form.add(
                label.setLabel(new ResourceModel("business.referenceData.label")).setRequired(true),
                postalCode
                    .setLabel(new ResourceModel("business.city.postalCode"))
                    .setRequired(true),
                new CheckBox("enabled", BindingModel.of(model, Bindings.city().enabled()))
                    .setLabel(new ResourceModel("business.referenceData.enabled"))
                    .add(
                        Condition.isTrue(BindingModel.of(model, Bindings.city().disableable()))
                            .thenEnable())
                    .setOutputMarkupId(true))
            .add(new CityUnicityFormValidator(getModel(), label, postalCode)));

    return body;
  }
}
