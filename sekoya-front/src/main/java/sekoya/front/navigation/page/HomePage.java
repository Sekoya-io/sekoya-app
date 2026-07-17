package sekoya.front.navigation.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_SITE_WRITE;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.service.controller.ISimulationCalculControllerService;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.service.business.ISiteService;
import sekoya.front.SekoyaSession;
import sekoya.front.common.map.component.MapPanel;
import sekoya.front.common.map.model.MapPoint;
import sekoya.front.navigation.template.HomeTemplate;
import sekoya.front.simulation.component.SimulationMapSearchPanel;
import sekoya.front.simulation.component.SimulationSiteOffcanvasPanel;
import sekoya.front.simulation.page.SimulationListPage;
import sekoya.front.site.popup.SiteSavePopup;

public class HomePage extends HomeTemplate {

  private static final long serialVersionUID = -6767518941118385548L;

  @SpringBean private ISimulationCalculControllerService simulationCalculControllerService;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(HomePage.class);
  }

  @SpringBean private ISiteService siteService;

  private IModel<SimulationSearchDto> simulationSearchDtoModel =
      Model.of(new SimulationSearchDto());

  public HomePage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.simulation")));
    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.simulation.map"), HomePage.linkDescriptor()));

    IModel<Collection<MapPoint>> pointsModel =
        LoadableDetachableModel.of(
            () -> {
              Organisation organisation = SekoyaSession.get().getOrganisationModel().getObject();

              if (organisation == null) {
                return List.of();
              }

              return organisation.getSiteEnabled().stream()
                  .map(
                      s ->
                          MapPoint.of(
                              s,
                              simulationCalculControllerService.getSiteRisqueBrut(
                                  s, simulationSearchDtoModel.getObject())))
                  .flatMap(Optional::stream)
                  .toList();
            });

    SimulationSiteOffcanvasPanel offcanvasPanel =
        new SimulationSiteOffcanvasPanel("offcanvas", simulationSearchDtoModel);

    MapPanel mapPanel =
        new MapPanel("map", pointsModel) {
          @Override
          protected void onPointClick(AjaxRequestTarget target, Long pointId) {
            Site site = siteService.getById(pointId);
            offcanvasPanel.onShow(target, site);
            centerOnPoint(target, pointId);
          }
        };

    SimulationMapSearchPanel search =
        new SimulationMapSearchPanel("search", simulationSearchDtoModel);

    BlankLink siteAdd = new BlankLink("siteAdd");

    SiteSavePopup siteAddPopup =
        new SiteSavePopup("siteAddPopup") {
          @Override
          protected void onSuccess(AjaxRequestTarget target, IModel<Site> siteModel) {
            target.add(getPage());
          }
        };
    add(siteAddPopup);

    add(
        offcanvasPanel,
        mapPanel,
        search.add(Condition.collectionModelNotEmpty(pointsModel).thenShow()),
        SimulationListPage.linkDescriptor().link("simulationListLink"),
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
                        .otherwise(Model.of("map-btn-fab-center"))))
            .add(Condition.permission(GLOBAL_SITE_WRITE).thenShow()));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(simulationSearchDtoModel);
  }
}
