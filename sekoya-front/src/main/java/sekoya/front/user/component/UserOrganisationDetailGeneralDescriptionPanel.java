package sekoya.front.user.component;

import static sekoya.back.security.model.SekoyaPermissionConstants.USER_WRITE;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DefaultPlaceholderPanel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.user.model.User;
import sekoya.back.util.binding.Bindings;
import sekoya.front.user.popup.UserOrganisationSavePopup;

public class UserOrganisationDetailGeneralDescriptionPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public UserOrganisationDetailGeneralDescriptionPanel(String id, IModel<User> userModel) {
    super(id, userModel);

    UserOrganisationSavePopup editPopup = new UserOrganisationSavePopup("editPopup");
    add(editPopup);

    IModel<String> emailAddressValueModel =
        BindingModel.of(userModel, Bindings.user().emailAddress().value());

    add(
        new CoreLabel("username", BindingModel.of(userModel, Bindings.user().username()))
            .showPlaceholder(),
        new BooleanIcon("enabled", BindingModel.of(userModel, Bindings.user().enabled())),
        new EmailLink("emailAddress", emailAddressValueModel),
        new CoreLabel(
                "organisation",
                BindingModel.of(userModel, Bindings.user().userOrganisation().organisation()))
            .showPlaceholder(),
        new DefaultPlaceholderPanel("emailAddressPlaceholder")
            .condition(Condition.modelNotNull(emailAddressValueModel)),
        new CoreLabel("creation", BindingModel.of(userModel, Bindings.user().creation()))
            .showPlaceholder(),
        new CoreLabel("modification", BindingModel.of(userModel, Bindings.user().modification()))
            .showPlaceholder(),
        new CoreLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()))
            .showPlaceholder());

    add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new BlankLink("edit")
                    .add(
                        new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onShow(AjaxRequestTarget target) {
                            editPopup.setUpEdit(getModelObject());
                          }
                        })
                    .add(Condition.permission(userModel, USER_WRITE).thenShow())));
  }
}
