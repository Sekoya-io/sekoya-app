package sekoya.front.user.component.tab;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.user.model.User;
import sekoya.front.user.component.UserDetailHistoryHistoryLogsPanel;

public class BasicUserDetailTabHistoryPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public BasicUserDetailTabHistoryPanel(String id, final IModel<User> userModel) {
    super(id, userModel);

    add(new UserDetailHistoryHistoryLogsPanel("historyLogs", userModel));
  }
}
