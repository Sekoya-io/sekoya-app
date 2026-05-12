package sekoya.front.security.login.component;

import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.security.service.controller.ISecurityManagementControllerService;
import sekoya.front.security.password.page.SecurityPasswordRecoveryRequestResetPage;

public class SignInFooterPanel extends Panel {

  private static final long serialVersionUID = -7042210777928535702L;

  @SpringBean private ISecurityManagementControllerService securityManagementController;

  public SignInFooterPanel(String wicketId) {
    super(wicketId);

    add(Condition.anyChildVisible(this).thenShow());

    add(
        SecurityPasswordRecoveryRequestResetPage.linkDescriptor()
            .link("passwordRecoveryRequestReset")
            .add(
                Condition.isTrue(
                        () ->
                            securityManagementController
                                .getSecurityOptions(UserType.ORGANISATION)
                                .isPasswordUserRecoveryEnabled())
                    .thenShow()));
  }
}
