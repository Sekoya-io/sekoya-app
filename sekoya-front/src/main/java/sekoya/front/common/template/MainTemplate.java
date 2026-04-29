package sekoya.front.common.template;

import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;
import static sekoya.front.property.SekoyaFrontPropertyIds.MAINTENANCE_URL;

import igloo.bootstrap.BootstrapRequestCycle;
import igloo.bootstrap.tooltip.BootstrapTooltipBehavior;
import igloo.bootstrap.tooltip.BootstrapTooltipOptions;
import igloo.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import sekoya.back.security.model.SekoyaAuthorityConstants;
import sekoya.back.security.service.ISekoyaAuthenticationService;
import sekoya.back.security.service.controller.ISecurityManagementControllerService;
import sekoya.front.SekoyaApplication;
import sekoya.front.SekoyaSession;
import sekoya.front.announcement.page.AnnouncementListPage;
import sekoya.front.common.component.AnnouncementsPanel;
import sekoya.front.common.template.resources.styles.application.application.applicationadvanced.StylesScssResourceReference;
import sekoya.front.common.template.theme.advanced.NavbarPanel;
import sekoya.front.common.template.theme.advanced.SidebarPanel;
import sekoya.front.common.template.theme.common.BootstrapBreakpointPanel;
import sekoya.front.referencedata.page.ReferenceDataPage;
import sekoya.front.role.page.RoleListPage;
import sekoya.front.security.password.page.SecurityPasswordExpirationPage;
import sekoya.front.user.page.BasicUserListPage;
import sekoya.front.user.page.TechnicalUserListPage;

public abstract class MainTemplate extends AbstractWebPageTemplate {

  private static final long serialVersionUID = -1312989780696228848L;

  @SpringBean private IPropertyService propertyService;

  @SpringBean private ISecurityManagementControllerService securityManagementController;

  @SpringBean private ISekoyaAuthenticationService authenticationService;

  protected MainTemplate(PageParameters parameters) {
    super(parameters);

    if (Boolean.TRUE.equals(propertyService.get(MAINTENANCE))
        && !authenticationService.hasAdminRole()) {
      throw new RedirectToUrlException(propertyService.get(MAINTENANCE_URL));
    }

    if (SekoyaSession.get().getOriginalAuthentication() == null
        && securityManagementController.isPasswordExpired(SekoyaSession.get().getUser())) {
      throw SecurityPasswordExpirationPage.linkDescriptor().newRestartResponseException();
    }

    add(
        new TransparentWebMarkupContainer("htmlElement")
            .add(AttributeAppender.append("lang", SekoyaSession.get().getLocale().getLanguage())));

    add(
        new TransparentWebMarkupContainer("bodyElement")
            .add(new ClassAttributeAppender(SekoyaSession.get().getEnvironmentModel())));

    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(new ResourceModel("common.application.name")));
    add(createHeadPageTitle("headPageTitle"));

    add(new AnnouncementsPanel("announcements"));

    add(createBodyBreadCrumb("breadCrumb").add(displayBreadcrumb().thenShow()));

    add(new AnimatedGlobalFeedbackPanel("feedback"));

    add(new BootstrapBreakpointPanel("bsBreakpoint"));

    add(new BootstrapTooltipBehavior(getBootstrapTooltipOptionsModel()));

    add(
        new NavbarPanel(
            "navbar", (SerializableSupplier2<Class<? extends WebPage>>) this::getFirstMenuPage),
        new SidebarPanel(
            "sidebar",
            (SerializableSupplier2<List<NavigationMenuItem>>) this::getMainNav,
            (SerializableSupplier2<Class<? extends WebPage>>) this::getFirstMenuPage,
            (SerializableSupplier2<Class<? extends WebPage>>) this::getSecondMenuPage));
  }

  protected List<NavigationMenuItem> getMainNav() {
    return List.of(
        SekoyaApplication.get()
            .getHomePageLinkDescriptor()
            .navigationMenuItem(new ResourceModel("navigation.home"))
            .iconClasses(Model.of("fa fa-fw fa-home")),
        ReferenceDataPage.linkDescriptor()
            .navigationMenuItem(new ResourceModel("navigation.referenceData"))
            .iconClasses(Model.of("fa fa-fw fa-list")),
        new NavigationMenuItem(new ResourceModel("navigation.administration"))
            .iconClasses(Model.of("fa fa-fw fa-cogs"))
            .subMenuForceOpen()
            .subMenuItems(
                BasicUserListPage.linkDescriptor()
                    .navigationMenuItem(new ResourceModel("navigation.administration.basicUser")),
                TechnicalUserListPage.linkDescriptor()
                    .navigationMenuItem(
                        new ResourceModel("navigation.administration.technicalUser")),
                RoleListPage.linkDescriptor()
                    .navigationMenuItem(new ResourceModel("navigation.administration.role")),
                AnnouncementListPage.linkDescriptor()
                    .navigationMenuItem(
                        new ResourceModel("navigation.administration.announcement"))),
        LinkDescriptorBuilder.start()
            .validator(Condition.role(SekoyaAuthorityConstants.ROLE_ADMIN))
            .page(ConsoleMaintenanceSearchPage.class)
            .navigationMenuItem(new ResourceModel("navigation.console"))
            .iconClasses(Model.of("fa fa-fw fa-wrench")));
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return null;
  }

  @Override
  protected Component createBodyBreadCrumb(String wicketId) {
    // By default, we remove one element from the breadcrumb as it is usually also used to generate
    // the page title.
    // The last element is usually the title of the current page and shouldn't be displayed in the
    // breadcrumb.
    return new BodyBreadCrumbPanel(
            wicketId, bodyBreadCrumbPrependedElementsModel, breadCrumbElementsModel, 1)
        .setDividerModel(Model.of(""))
        .setTrailingSeparator(true);
  }

  protected Condition displayBreadcrumb() {
    return Condition.alwaysTrue();
  }

  protected IModel<BootstrapTooltipOptions> getBootstrapTooltipOptionsModel() {
    return BootstrapTooltipOptions::get;
  }

  @Override
  public void renderHead(IHeaderResponse response) {
    super.renderHead(response);
    response.render(CssHeaderItem.forReference(StylesScssResourceReference.get()));
    BootstrapRequestCycle.getSettings().renderHead(getPage(), response);
  }
}
