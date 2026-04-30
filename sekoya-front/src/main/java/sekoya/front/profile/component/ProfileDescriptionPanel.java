package sekoya.front.profile.component;

import static sekoya.back.security.model.SekoyaPermissionConstants.USER_EDIT_PASSWORD;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DefaultPlaceholderPanel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.user.model.User;
import sekoya.back.security.service.controller.ISecurityManagementControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.user.popup.UserPasswordEditPopup;

public class ProfileDescriptionPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ISecurityManagementControllerService securityManagementController;

  public ProfileDescriptionPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    UserPasswordEditPopup passwordEditPopup =
        new UserPasswordEditPopup("passwordEditPopup", userModel);
    add(passwordEditPopup);

    IModel<String> emailAddressValueModel =
        BindingModel.of(userModel, Bindings.user().emailAddress().value());

    add(
        new BlankLink("passwordEdit")
            .add(new AjaxModalOpenBehavior(passwordEditPopup, MouseEvent.CLICK))
            .add(Condition.permission(userModel, USER_EDIT_PASSWORD).thenShow()),
        new CoreLabel("username", BindingModel.of(userModel, Bindings.user().username()))
            .showPlaceholder(),
        new BooleanIcon("enabled", BindingModel.of(userModel, Bindings.user().enabled())),
        new EmailLink("emailAddress", emailAddressValueModel),
        new DefaultPlaceholderPanel("emailAddressPlaceholder")
            .condition(Condition.modelNotNull(emailAddressValueModel)),
        new CoreLabel("creationDate", BindingModel.of(userModel, Bindings.user().creation().date()))
            .showPlaceholder(),
        new CoreLabel(
                "modificationDate",
                BindingModel.of(userModel, Bindings.user().modification().date()))
            .showPlaceholder(),
        new CoreLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()))
            .showPlaceholder());
  }
}
