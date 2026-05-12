package sekoya.front.user.component.tab;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.user.model.User;
import sekoya.front.user.component.UserAdministrateurFonctionnelDetailGeneralDescriptionPanel;

public class UserAdministrateurFonctionnelDetailTabGeneralPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public UserAdministrateurFonctionnelDetailTabGeneralPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    add(new UserAdministrateurFonctionnelDetailGeneralDescriptionPanel("description", userModel));
  }
}
