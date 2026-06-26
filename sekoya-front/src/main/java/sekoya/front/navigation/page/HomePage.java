package sekoya.front.navigation.page;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.service.business.ISiteService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.SekoyaSession;
import sekoya.front.common.map.component.MapPanel;
import sekoya.front.common.map.model.MapPoint;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.site.page.SiteDetailPage;
import sekoya.front.site.popup.SiteSavePopup;

public class HomePage extends MainTemplate {

  private static final long serialVersionUID = -6767518941118385548L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(HomePage.class);
  }

  @SpringBean private ISiteService siteService;

  public HomePage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("navigation.home"), HomePage.linkDescriptor()));

    getBodyElement().add(new ClassAttributeAppender(Model.of("sidebar-expand-0")));

    IModel<Collection<MapPoint>> pointsModel =
        LoadableDetachableModel.of(
            () -> {
              Organisation organisation = SekoyaSession.get().getOrganisationModel().getObject();

              if (organisation == null) {
                return List.of();
              }

              return organisation.getSites().stream()
                  .filter(Predicates2.compose(Predicates2.isTrue(), Bindings.site().enabled()))
                  .map(MapPoint::of)
                  .flatMap(Optional::stream)
                  .toList();
            });

    MapPanel map =
        new MapPanel("map", pointsModel) {
          @Override
          protected void onPointClick(AjaxRequestTarget target, Long pointId) {
            Site site = siteService.getById(pointId);
            throw SiteDetailPage.MAPPER
                .map(GenericEntityModel.of(site))
                .newRestartResponseException();
          }
        };

    BlankLink siteAdd = new BlankLink("siteAdd");

    SiteSavePopup siteAddPopup =
        new SiteSavePopup("siteAddPopup") {
          @Override
          protected void onSuccess(AjaxRequestTarget target, IModel<Site> siteModel) {
            target.add(map, siteAdd);
          }
        };
    add(siteAddPopup);

    add(
        map,
        siteAdd
            .add(
                new AjaxModalOpenBehavior(siteAddPopup, MouseEvent.CLICK) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void onShow(AjaxRequestTarget target) {
                    siteAddPopup.setUpAdd(new Site());
                  }
                })
            .add(
                new ClassAttributeAppender(
                    Condition.collectionModelNotEmpty(pointsModel)
                        .then(Model.of("map-btn-fab-bottom"))
                        .otherwise(Model.of("map-btn-fab-center")))));
  }

  @Override
  protected Condition displayBreadcrumb() {
    return Condition.alwaysFalse();
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return HomePage.class;
  }
}
