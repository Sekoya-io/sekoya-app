package sekoya.front.user.component.tab;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.user.model.User;
import sekoya.front.user.component.TechnicalUserDetailGeneralDescriptionPanel;

public class TechnicalUserDetailTabGeneralPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public TechnicalUserDetailTabGeneralPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    add(new TechnicalUserDetailGeneralDescriptionPanel("description", userModel));
  }
}
