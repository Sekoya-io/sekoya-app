package sekoya.front.role.component;

import igloo.wicket.model.BindingModel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import sekoya.back.business.role.model.Role;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.validator.RoleTitleUnicityValidator;

public class RoleSaveTitlePanel extends Panel {

  private static final long serialVersionUID = 1L;

  public RoleSaveTitlePanel(String id, IModel<Role> roleModel) {
    super(id);

    add(
        new TextField<>("title", BindingModel.of(roleModel, Bindings.role().title()))
            .setRequired(true)
            .setLabel(new ResourceModel("business.role.title"))
            .add(new RoleTitleUnicityValidator(roleModel)));
  }
}
