package sekoya.front.role.component;

import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.service.controller.IRoleControllerService;
import sekoya.front.role.page.RoleDetailPage;
import sekoya.front.role.page.RoleListPage;

public class RoleSaveFooterPanel extends GenericPanel<Role> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IRoleControllerService roleControllerService;

  private static final Logger LOGGER = LoggerFactory.getLogger(RoleSaveFooterPanel.class);

  public RoleSaveFooterPanel(String id, IModel<Role> roleModel) {
    super(id);

    add(
        new AjaxLink<>("cancel") {
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {
            if (roleModel.getObject().isNew()) {
              throw RoleListPage.linkDescriptor().newRestartResponseException();
            } else {
              throw RoleDetailPage.MAPPER.map(roleModel).newRestartResponseException();
            }
          }
        },
        new AjaxButton("save") {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              roleControllerService.saveRole(roleModel.getObject());
              Session.get().success(getString("common.success"));
              throw RoleDetailPage.MAPPER.map(roleModel).newRestartResponseException();
            } catch (RestartResponseException e) {
              throw e;
            } catch (Exception e) {
              Session.get().error(getString("common.error.unexpected"));
              LOGGER.error("Erreur lors de la saisie d'un rôle.", e);
            }
            FeedbackUtils.refreshFeedback(target, getPage());
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        });
  }
}
