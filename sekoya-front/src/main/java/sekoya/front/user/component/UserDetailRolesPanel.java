package sekoya.front.user.component;

import static sekoya.back.security.model.SekoyaPermissionConstants.USER_WRITE;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.user.model.User;
import sekoya.front.role.model.RoleDataProvider;
import sekoya.front.user.popup.UserRolesEditPopup;

public class UserDetailRolesPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  public UserDetailRolesPanel(String id, final IModel<? extends User> userModel) {
    super(id, userModel);

    UserRolesEditPopup editPopup = new UserRolesEditPopup("editPopup", getModel());
    add(editPopup);

    add(
        new SequenceView<>("roles", new RoleDataProvider()) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(Item<Role> item) {
            item.add(
                new CoreLabel("name", item.getModel()),
                new BooleanIcon(
                    "check",
                    () -> userModel.getObject().getRoles().contains(item.getModelObject())));
          }
        });

    add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new BlankLink("edit")
                    .add(new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK))
                    .add(Condition.permission(userModel, USER_WRITE).thenShow())));
  }
}
