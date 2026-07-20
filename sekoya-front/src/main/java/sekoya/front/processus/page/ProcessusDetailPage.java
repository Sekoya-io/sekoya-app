package sekoya.front.processus.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_DISABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_ENABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_WRITE;

import igloo.bootstrap.confirm.AjaxConfirmLink;
import igloo.bootstrap5.markup.html.bootstrap.component.BootstrapBadge;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.service.controller.IProcessusControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.processus.component.ProcessusDetailAleasPanel;
import sekoya.front.processus.component.ProcessusDetailDescriptionPanel;
import sekoya.front.processus.renderer.ProcessusBootstrapRenderer;
import sekoya.front.processus.template.ProcessusTemplate;
import sekoya.front.user.page.UserAdministrateurFonctionnelListPage;

public class ProcessusDetailPage extends ProcessusTemplate {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessusDetailPage.class);

  public static final IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, Processus> MAPPER =
      LinkDescriptorBuilder.start()
          .model(Processus.class)
          .permission(PROCESSUS_READ)
          .map(CommonParameters.ID)
          .mandatory()
          .page(ProcessusDetailPage.class);

  @SpringBean protected IProcessusControllerService processusControllerService;

  private final IModel<Processus> processusModel = new GenericEntityModel<>();

  public ProcessusDetailPage(PageParameters parameters) {
    super(parameters);

    MAPPER
        .map(processusModel)
        .extractSafely(
            parameters,
            UserAdministrateurFonctionnelListPage.linkDescriptor(),
            getString("common.error.unexpected"));

    addBreadCrumbElement(
        new BreadCrumbElement(BindingModel.of(processusModel, Bindings.processus().nom())));

    add(
        new WebMarkupContainer("icon")
            .add(
                new ClassAttributeAppender(
                    BindingModel.of(
                        processusModel, Bindings.processus().thematique().iconCssClass()))),
        new CoreLabel("title", BindingModel.of(processusModel, Bindings.processus().nom())));

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("informationContainer")
            .anyChildVisible()
            .add(
                new BootstrapBadge<>(
                        "enabled", processusModel, ProcessusBootstrapRenderer.enabled())
                    .badgePill()));

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new AjaxLink<>("enable", processusModel) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    try {
                      processusControllerService.enable(processusModel.getObject());
                      Session.get().success(getString("common.success"));
                      target.add(getPage());
                    } catch (Exception e) {
                      LOGGER.error("Erreur activation site", e);
                      Session.get().error(getString("common.error.unexpected"));
                    }
                    FeedbackUtils.refreshFeedback(target, getPage());
                  }
                }.add(Condition.permission(processusModel, PROCESSUS_ENABLE).thenShow()),
                ProcessusEditPage.MAPPER
                    .map(processusModel)
                    .link("edit")
                    .add(Condition.permission(processusModel, PROCESSUS_WRITE).thenShow()),
                AjaxConfirmLink.<Processus>build()
                    .title(new ResourceModel("common.action.disable"))
                    .content(new ResourceModel("common.action.confirm.content"))
                    .confirm()
                    .onClick(
                        new IAjaxAction() {
                          private static final long serialVersionUID = 1L;

                          @Override
                          public void execute(AjaxRequestTarget target) {
                            try {
                              processusControllerService.disable(processusModel.getObject());
                              Session.get().success(getString("common.success"));
                            } catch (Exception e) {
                              LOGGER.error("Erreur désactivation site", e);
                              Session.get().error(getString("common.error.unexpected"));
                            }
                            target.add(getPage());
                            FeedbackUtils.refreshFeedback(target, getPage());
                          }
                        })
                    .create("disable", processusModel)
                    .add(Condition.permission(processusModel, PROCESSUS_DISABLE).thenShow())));

    add(
        new ProcessusDetailDescriptionPanel("description", processusModel),
        new ProcessusDetailAleasPanel("aleas", processusModel));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(processusModel);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ProcessusDetailPage.class;
  }
}
