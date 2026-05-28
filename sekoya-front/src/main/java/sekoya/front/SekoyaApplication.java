package sekoya.front;

import igloo.console.navigation.page.ConsoleAccessDeniedPage;
import igloo.console.navigation.page.ConsoleLoginFailurePage;
import igloo.console.navigation.page.ConsoleLoginSuccessPage;
import igloo.console.navigation.page.ConsoleSignInPage;
import igloo.console.template.ConsoleConfiguration;
import igloo.wicket.convert.EnumClassAwareConverterLocator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import org.apache.wicket.Application;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventValue;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.application.CoreWicketAuthenticatedApplication;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuSection;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.pages.monitoring.DatabaseMonitoringPage;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.iglooproject.wicket.more.rendering.LocaleRenderer;
import org.iglooproject.wicket.more.security.page.LoginFailurePage;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.util.convert.HibernateProxyAwareConverterLocator;
import org.iglooproject.wicket.more.util.listener.FormInvalidDecoratorListener;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.CodePostal;
import sekoya.back.business.common.model.EmailAddress;
import sekoya.back.business.common.model.Latitude;
import sekoya.back.business.common.model.Longitude;
import sekoya.back.business.common.model.embeddable.Adresse;
import sekoya.back.business.history.model.atomic.HistoryLogEventType;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusPriorite;
import sekoya.back.business.processus.model.atomic.ProcessusThematique;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.model.Departement;
import sekoya.back.business.referencedata.model.Region;
import sekoya.back.business.referencedata.model.atomic.CommuneTypeInsee;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.model.atomic.SiteTypologie;
import sekoya.back.business.user.model.User;
import sekoya.front.announcement.page.AnnouncementListPage;
import sekoya.front.common.converter.CodePostalConverter;
import sekoya.front.common.converter.EmailAddressConverter;
import sekoya.front.common.converter.LatitudeConverter;
import sekoya.front.common.converter.LocalDateConverter;
import sekoya.front.common.converter.LocalDateTimeConverter;
import sekoya.front.common.converter.LocalTimeConverter;
import sekoya.front.common.converter.LongitudeConverter;
import sekoya.front.common.converter.PointConverter;
import sekoya.front.common.renderer.AdresseRenderer;
import sekoya.front.common.renderer.CommuneRenderer;
import sekoya.front.common.renderer.HistoryEventSummaryRenderer;
import sekoya.front.common.renderer.InstantRenderer;
import sekoya.front.common.renderer.RoleRenderer;
import sekoya.front.common.template.favicon.ApplicationFaviconPackage;
import sekoya.front.common.template.resources.SekoyaResourcesPackage;
import sekoya.front.common.template.resources.styles.application.application.applicationaccess.ApplicationAccessScssResourceReference;
import sekoya.front.common.template.resources.styles.application.application.applicationadvanced.StylesScssResourceReference;
import sekoya.front.common.template.resources.styles.application.console.console.ConsoleScssResourceReference;
import sekoya.front.common.template.resources.styles.application.console.consoleaccess.ConsoleAccessScssResourceReference;
import sekoya.front.common.template.resources.styles.notification.email.NotificationEmailScssResourceReference;
import sekoya.front.common.template.resources.styles.notification.head.NotificationHeadScssResourceReference;
import sekoya.front.console.common.component.ConsoleAccessHeaderAdditionalContentPanel;
import sekoya.front.console.common.component.ConsoleHeaderAdditionalContentPanel;
import sekoya.front.console.common.component.ConsoleHeaderEnvironmentPanel;
import sekoya.front.console.notification.demo.page.ConsoleNotificationDemoListPage;
import sekoya.front.history.renderer.IHistoryValueRenderer;
import sekoya.front.navigation.page.HomePage;
import sekoya.front.navigation.page.MaintenancePage;
import sekoya.front.organisation.page.OrganisationListPage;
import sekoya.front.organisation.renderer.OrganisationRenderer;
import sekoya.front.processus.page.ProcessusAddPage;
import sekoya.front.processus.page.ProcessusDetailPage;
import sekoya.front.processus.page.ProcessusEditPage;
import sekoya.front.processus.page.ProcessusListPage;
import sekoya.front.processus.page.ProcessusSiteAddPage;
import sekoya.front.processus.renderer.AleaRenderer;
import sekoya.front.processus.renderer.ProcessusRenderer;
import sekoya.front.profile.page.ProfilePage;
import sekoya.front.referencedata.page.ReferenceDataPage;
import sekoya.front.referencedata.renderer.ReferenceDataRenderer;
import sekoya.front.resources.application.SekoyaApplicationResources;
import sekoya.front.resources.business.SekoyaBusinessResources;
import sekoya.front.resources.common.SekoyaCommonResources;
import sekoya.front.resources.console.SekoyaConsoleResources;
import sekoya.front.resources.enums.SekoyaEnumsResources;
import sekoya.front.resources.navigation.SekoyaNavigationResources;
import sekoya.front.resources.notification.SekoyaNotificationResources;
import sekoya.front.security.login.page.SignInPage;
import sekoya.front.security.password.page.SecurityPasswordCreationPage;
import sekoya.front.security.password.page.SecurityPasswordExpirationPage;
import sekoya.front.security.password.page.SecurityPasswordRecoveryRequestResetPage;
import sekoya.front.security.password.page.SecurityPasswordResetPage;
import sekoya.front.site.page.SiteDetailPage;
import sekoya.front.site.page.SiteListPage;
import sekoya.front.site.renderer.SiteRenderer;
import sekoya.front.user.page.UserAdministrateurFonctionnelDetailPage;
import sekoya.front.user.page.UserAdministrateurFonctionnelListPage;
import sekoya.front.user.page.UserOrganisationDetailPage;
import sekoya.front.user.page.UserOrganisationListPage;
import sekoya.front.user.renderer.UserRenderer;

public class SekoyaApplication extends CoreWicketAuthenticatedApplication {

  public static final String NAME = "SekoyaApplication";

  @Autowired private IPropertyService propertyService;

  public static SekoyaApplication get() {
    final Application application = Application.get();
    if (application instanceof SekoyaApplication) {
      return (SekoyaApplication) application;
    }
    throw new WicketRuntimeException(
        "There is no SekoyaApplication attached to current thread "
            + Thread.currentThread().getName());
  }

  @Override
  public void init() {
    super.init();

    // si on n'est pas en développement, on précharge les feuilles de styles pour éviter la ruée et
    // permettre le remplissage du cache
    if (!propertyService.isConfigurationTypeDevelopment()) {
      preloadStyleSheets(
          ConsoleAccessScssResourceReference.get(),
          ConsoleScssResourceReference.get(),
          NotificationEmailScssResourceReference.get(),
          NotificationHeadScssResourceReference.get(),
          ApplicationAccessScssResourceReference.get(),
          StylesScssResourceReference.get());
    }

    getResourceSettings()
        .getStringResourceLoaders()
        .addAll(
            0, // Override the keys in existing resource loaders with the following
            List.of(
                new ClassStringResourceLoader(SekoyaApplicationResources.class),
                new ClassStringResourceLoader(SekoyaBusinessResources.class),
                new ClassStringResourceLoader(SekoyaCommonResources.class),
                new ClassStringResourceLoader(SekoyaConsoleResources.class),
                new ClassStringResourceLoader(SekoyaEnumsResources.class),
                new ClassStringResourceLoader(SekoyaNavigationResources.class),
                new ClassStringResourceLoader(SekoyaNotificationResources.class)));

    FormInvalidDecoratorListener.init(this);
  }

  @Override
  protected IConverterLocator newConverterLocator() {
    ConverterLocator converterLocator = new ConverterLocator();

    converterLocator.set(LocalDate.class, LocalDateConverter.get());
    converterLocator.set(LocalDateTime.class, LocalDateTimeConverter.get());
    converterLocator.set(LocalTime.class, LocalTimeConverter.get());
    converterLocator.set(Instant.class, InstantRenderer.get());
    converterLocator.set(Locale.class, LocaleRenderer.get());
    converterLocator.set(boolean.class, BooleanRenderer.get());
    converterLocator.set(Boolean.class, BooleanRenderer.get());

    converterLocator.set(Point.class, PointConverter.get());
    converterLocator.set(EmailAddress.class, EmailAddressConverter.get());
    converterLocator.set(CodePostal.class, CodePostalConverter.get());
    converterLocator.set(Latitude.class, LatitudeConverter.get());
    converterLocator.set(Longitude.class, LongitudeConverter.get());

    converterLocator.set(HistoryEventSummary.class, HistoryEventSummaryRenderer.complet());
    converterLocator.set(Adresse.class, AdresseRenderer.get());

    converterLocator.set(Commune.class, CommuneRenderer.get());
    converterLocator.set(CommuneTypeInsee.class, EnumRenderer.get());
    converterLocator.set(Departement.class, ReferenceDataRenderer.get());
    converterLocator.set(Region.class, ReferenceDataRenderer.get());

    converterLocator.set(Organisation.class, OrganisationRenderer.get());
    converterLocator.set(Site.class, SiteRenderer.get());
    converterLocator.set(SiteTypologie.class, EnumRenderer.get());
    converterLocator.set(Processus.class, ProcessusRenderer.get());
    converterLocator.set(ProcessusThematique.class, EnumRenderer.get());
    converterLocator.set(ProcessusType.class, EnumRenderer.get());
    converterLocator.set(ProcessusPriorite.class, EnumRenderer.get());
    converterLocator.set(Alea.class, AleaRenderer.get());
    converterLocator.set(AleaType.class, EnumRenderer.get());

    converterLocator.set(User.class, UserRenderer.get());
    converterLocator.set(Role.class, RoleRenderer.get());

    converterLocator.set(HistoryValue.class, IHistoryValueRenderer.get());
    converterLocator.set(HistoryEventValue.class, IHistoryValueRenderer.get());
    converterLocator.set(HistoryLogEventType.class, EnumRenderer.get());

    return new EnumClassAwareConverterLocator(
        new HibernateProxyAwareConverterLocator(converterLocator));
  }

  @Override
  protected void mountApplicationPages() {

    // Sign in
    mountPage("/login/", getSignInPageClass());
    mountPage("/login/failure/", LoginFailurePage.class);
    mountPage("/login/success/", LoginSuccessPage.class);

    mountPage(
        "/security/password/recovery/request/reset/",
        SecurityPasswordRecoveryRequestResetPage.class);
    mountPage("/security/password/expiration/", SecurityPasswordExpirationPage.class);
    mountParameterizedPage("/security/password/creation/", SecurityPasswordCreationPage.class);
    mountParameterizedPage("/security/password/reset/", SecurityPasswordResetPage.class);

    // Maintenance
    mountPage("/maintenance/", MaintenancePage.class);

    // Profile
    mountPage("/profil/", ProfilePage.class);

    // Organisation
    mountPage("/organisation/", OrganisationListPage.class);

    // Site
    mountPage("/site/", SiteListPage.class);
    mountParameterizedPage("/site/${" + CommonParameters.ID + "}/", SiteDetailPage.class);
    mountParameterizedPage(
        "/site/${" + CommonParameters.ID + "}/processus/creation/", ProcessusSiteAddPage.class);

    // Processus
    mountPage("/processus/", ProcessusListPage.class);
    mountParameterizedPage("/processus/${" + CommonParameters.ID + "}/", ProcessusDetailPage.class);
    mountPage("/processus/creation/", ProcessusAddPage.class);
    mountParameterizedPage(
        "/processus/${" + CommonParameters.ID + "}/modification/", ProcessusEditPage.class);

    // Reference data
    mountPage("/referentiel/", ReferenceDataPage.class);

    // Administration
    mountPage("/administration/utilisateur-organisation/", UserOrganisationListPage.class);
    mountParameterizedPage(
        "/administration/utilisateur-organisation/${" + CommonParameters.ID + "}/",
        UserOrganisationDetailPage.class);
    mountPage("/administration/administrateur/", UserAdministrateurFonctionnelListPage.class);
    mountParameterizedPage(
        "/administration/administrateur/${" + CommonParameters.ID + "}/",
        UserAdministrateurFonctionnelDetailPage.class);
    mountPage("/administration/annonce/", AnnouncementListPage.class);

    // Console sign in
    mountPage("/console/login/", ConsoleSignInPage.class);
    mountPage("/console/login/failure/", ConsoleLoginFailurePage.class);
    mountPage("/console/login/success/", ConsoleLoginSuccessPage.class);
    mountPage("/console/access-denied/", ConsoleAccessDeniedPage.class);

    // Console
    ConsoleConfiguration consoleConfiguration =
        ConsoleConfiguration.build("console", getResourceSettings());
    consoleConfiguration.addCssResourceReference(ConsoleScssResourceReference.get());
    consoleConfiguration.addConsoleAccessCssResourceReference(
        ConsoleAccessScssResourceReference.get());
    consoleConfiguration.setConsoleAccessHeaderAdditionalContentComponentFactory(
        ConsoleAccessHeaderAdditionalContentPanel::new);
    consoleConfiguration.setConsoleHeaderEnvironmentComponentFactory(
        ConsoleHeaderEnvironmentPanel::new);
    consoleConfiguration.setConsoleHeaderAdditionalContentComponentFactory(
        ConsoleHeaderAdditionalContentPanel::new);

    ConsoleMenuSection notificationMenuSection =
        new ConsoleMenuSection(
            "notificationMenuSection",
            "console.navigation.notification",
            "notification",
            ConsoleNotificationDemoListPage.class);
    consoleConfiguration.addMenuSection(notificationMenuSection);

    consoleConfiguration.mountPages(this);

    // Monitoring
    mountPage("/monitoring/db-access/", DatabaseMonitoringPage.class);
  }

  @Override
  protected void mountApplicationResources() {
    mountStaticResourceDirectory("/application", SekoyaResourcesPackage.class);

    // See favicon generator https://realfavicongenerator.net/
    mountResource(
        "/android-chrome-192x192.png",
        new PackageResourceReference(
            ApplicationFaviconPackage.class, "android-chrome-192x192.png"));
    mountResource(
        "/android-chrome-256x256.png",
        new PackageResourceReference(
            ApplicationFaviconPackage.class, "android-chrome-256x256.png"));
    mountResource(
        "/apple-touch-icon.png",
        new PackageResourceReference(ApplicationFaviconPackage.class, "apple-touch-icon.png"));
    mountResource(
        "/browserconfig.xml",
        new PackageResourceReference(ApplicationFaviconPackage.class, "browserconfig.xml"));
    mountResource(
        "/favicon.ico",
        new PackageResourceReference(ApplicationFaviconPackage.class, "favicon.ico"));
    mountResource(
        "/favicon-16x16.png",
        new PackageResourceReference(ApplicationFaviconPackage.class, "favicon-16x16.png"));
    mountResource(
        "/favicon-32x32.png",
        new PackageResourceReference(ApplicationFaviconPackage.class, "favicon-32x32.png"));
    mountResource(
        "/mstile-150x150.png",
        new PackageResourceReference(ApplicationFaviconPackage.class, "mstile-150x150.png"));
    mountResource(
        "/safari-pinned-tab.svg",
        new PackageResourceReference(ApplicationFaviconPackage.class, "safari-pinned-tab.svg"));
    mountResource(
        "/site.webmanifest",
        new PackageResourceReference(ApplicationFaviconPackage.class, "site.webmanifest"));
  }

  @Override
  protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
    return SekoyaSession.class;
  }

  @Override
  public Class<? extends Page> getHomePage() {
    return HomePage.class;
  }

  @Override
  public Class<? extends WebPage> getSignInPageClass() {
    return SignInPage.class;
  }
}
