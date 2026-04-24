package sekoya.front.user.component.tab;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.user.model.User;
import sekoya.front.user.component.BasicUserDetailGeneralDescriptionPanel;
import sekoya.front.user.component.UserDetailRolesPanel;

public class BasicUserDetailTabGeneralPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public BasicUserDetailTabGeneralPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    add(
        new BasicUserDetailGeneralDescriptionPanel("description", userModel),
        new UserDetailRolesPanel("roles", userModel));
  }
}
