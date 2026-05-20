package sekoya.front.site.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_DISABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_ENABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_READ;

import igloo.bootstrap.confirm.AjaxConfirmLink;
import igloo.bootstrap5.markup.html.bootstrap.component.BootstrapBadge;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.service.controller.ISiteControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.site.component.SiteDetailDescriptionPanel;
import sekoya.front.site.renderer.SiteBootstrapRenderer;
import sekoya.front.site.template.SiteTemplate;
import sekoya.front.user.page.UserAdministrateurFonctionnelListPage;

public class SiteDetailPage extends SiteTemplate {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(SiteDetailPage.class);

  public static final IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, Site> MAPPER =
      LinkDescriptorBuilder.start()
          .model(Site.class)
          .permission(SITE_READ)
          .map(CommonParameters.ID)
          .mandatory()
          .page(SiteDetailPage.class);

  @SpringBean protected ISiteControllerService siteControllerService;

  protected final IModel<Site> siteModel = new GenericEntityModel<>();

  public SiteDetailPage(PageParameters parameters) {
    super(parameters);

    MAPPER
        .map(siteModel)
        .extractSafely(
            parameters,
            UserAdministrateurFonctionnelListPage.linkDescriptor(),
            getString("common.error.unexpected"));

    addBreadCrumbElement(new BreadCrumbElement(BindingModel.of(siteModel, Bindings.site().nom())));

    add(new CoreLabel("title", BindingModel.of(siteModel, Bindings.site().nom())));

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("informationContainer")
            .anyChildVisible()
            .add(
                new BootstrapBadge<>("enabled", siteModel, SiteBootstrapRenderer.enabled())
                    .badgePill()));

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new AjaxLink<>("enable", siteModel) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    try {
                      siteControllerService.enable(siteModel.getObject());
                      Session.get().success(getString("common.success"));
                      target.add(getPage());
                    } catch (Exception e) {
                      LOGGER.error("Erreur activation site", e);
                      Session.get().error(getString("common.error.unexpected"));
                    }
                    FeedbackUtils.refreshFeedback(target, getPage());
                  }
                }.add(Condition.permission(siteModel, SITE_ENABLE).thenShow()),
                AjaxConfirmLink.<Site>build()
                    .title(new ResourceModel("common.action.disable"))
                    .content(new ResourceModel("common.action.confirm.content"))
                    .confirm()
                    .onClick(
                        new IAjaxAction() {
                          private static final long serialVersionUID = 1L;

                          @Override
                          public void execute(AjaxRequestTarget target) {
                            try {
                              siteControllerService.disable(siteModel.getObject());
                              Session.get().success(getString("common.success"));
                            } catch (Exception e) {
                              LOGGER.error("Erreur désactivation site", e);
                              Session.get().error(getString("common.error.unexpected"));
                            }
                            target.add(getPage());
                            FeedbackUtils.refreshFeedback(target, getPage());
                          }
                        })
                    .create("disable", siteModel)
                    .add(Condition.permission(siteModel, SITE_DISABLE).thenShow())));

    add(new SiteDetailDescriptionPanel("description", siteModel));
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return SiteDetailPage.class;
  }
}
