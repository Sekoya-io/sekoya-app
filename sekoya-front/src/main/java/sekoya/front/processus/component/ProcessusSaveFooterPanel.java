package sekoya.front.processus.component;

import igloo.wicket.feedback.FeedbackUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.ajax.AjaxListeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.business.processus.service.controller.IProcessusControllerService;
import sekoya.front.processus.model.ProcessusBindableModel;
import sekoya.front.processus.page.ProcessusDetailPage;
import sekoya.front.processus.page.ProcessusListPage;

public class ProcessusSaveFooterPanel extends AbstractProcessusSavePanel {

  private static final long serialVersionUID = 1L;

  public static final Logger LOGGER = LoggerFactory.getLogger(ProcessusSaveFooterPanel.class);

  @SpringBean private IProcessusControllerService processusControllerService;

  public ProcessusSaveFooterPanel(String id, ProcessusBindableModel processusBindableModel) {
    super(id, processusBindableModel);
    setOutputMarkupId(true);

    add(
        new AjaxLink<>("cancel") {
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {
            if (addCondition().applies()) {
              throw ProcessusListPage.linkDescriptor().newRestartResponseException();
            } else {
              throw ProcessusDetailPage.MAPPER
                  .map(processusBindableModel)
                  .newRestartResponseException();
            }
          }
        },
        new AjaxButton("save") {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              processusControllerService.saveProcessus(processusBindableModel.getObject());

              throw ProcessusDetailPage.MAPPER
                  .map(processusBindableModel)
                  .newRestartResponseException();
            } catch (RestartResponseException e) { // NOSONAR
              throw e;
            } catch (Exception e) {
              Session.get().error(getString("common.error.unexpected"));
              LOGGER.error("Erreur saisie processus", e);
            }

            FeedbackUtils.refreshFeedback(target, getPage());
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
            AjaxListeners.add(target, AjaxListeners.clearInput(getForm()));
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        });
  }
}
