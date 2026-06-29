package sekoya.front.simulation.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_SITE_WRITE;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.search.SiteSort;
import sekoya.back.business.site.service.controller.ISiteControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.SekoyaSession;
import sekoya.front.common.component.RisqueRatingDisplayPanel;
import sekoya.front.simulation.component.SimulationListSearchPanel;
import sekoya.front.simulation.component.SimulationSiteOffcanvasPanel;
import sekoya.front.simulation.template.SimulationTemplate;
import sekoya.front.site.model.SiteDataProvider;
import sekoya.front.site.popup.SiteSavePopup;

public class SimulationListPage extends SimulationTemplate {

  private static final long serialVersionUID = 1L;

  @SpringBean private ISiteControllerService siteControllerService;

  private IModel<SimulationSearchDto> simulationSearchDtoModel =
      Model.of(new SimulationSearchDto());

  // TODO : permissions ? Permissions HomePage ?
  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(SimulationListPage.class);
  }

  @SpringBean private IPropertyService propertyService;

  public SimulationListPage(PageParameters parameters) {
    super(parameters);
    add(Condition.modelNotNull(SekoyaSession.get().getOrganisationModel()).thenShow());

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.simulation.list"), SimulationListPage.linkDescriptor()));

    SiteDataProvider dataProvider = new SiteDataProvider();
    dataProvider
        .getDataModel()
        .getObject()
        .setOrganisation(SekoyaSession.get().getOrganisationModel().getObject());
    dataProvider.getDataModel().getObject().setEnabledFilter(EnabledFilter.ENABLED_ONLY);

    // TODO : refresh plus fin ?
    SiteSavePopup savePopup =
        new SiteSavePopup("savePopup") {
          @Override
          protected void onSuccess(AjaxRequestTarget target, IModel<Site> siteModel) {
            target.add(getPage());
          }
        };
    add(savePopup);

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new BlankLink("add")
                    .add(
                        new AjaxModalOpenBehavior(savePopup, MouseEvent.CLICK) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onShow(AjaxRequestTarget target) {
                            savePopup.setUpAdd(new Site());
                          }
                        })
                    .add(Condition.permission(GLOBAL_SITE_WRITE).thenShow())));

    SimulationSiteOffcanvasPanel offcanvasPanel =
        new SimulationSiteOffcanvasPanel("offcanvas", simulationSearchDtoModel);

    DecoratedCoreDataTablePanel<Site, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addColumn(
                new AbstractCoreColumn<Site, SiteSort>(new ResourceModel("business.site.nom")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Site>> cellItem,
                      String componentId,
                      IModel<Site> rowModel) {
                    cellItem.add(new NomCellFragment(componentId, rowModel, offcanvasPanel));
                  }
                })
            .withSort(SiteSort.NOM, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.site.typologie"), Bindings.site().typologie())
            .withClass("cell-w-200")
            .addLabelColumn(new ResourceModel("business.site.adresse"), Bindings.site().adresse())
            .multiline()
            .withClass("cell-w-300")
            // TODO : sort risque desc
            .addColumn(
                new AbstractCoreColumn<Site, SiteSort>(new ResourceModel("business.site.risque")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Site>> cellItem,
                      String componentId,
                      IModel<Site> rowModel) {
                    cellItem.add(
                        new RisqueCellFragment(componentId, rowModel, simulationSearchDtoModel));
                  }
                })
            .withClass("cell-w-250")
            .rows()
            .end()
            .bootstrapCard()
            .ajaxPagers()
            .count("site.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(
        new SimulationListSearchPanel("search", dataProvider, results, simulationSearchDtoModel),
        results,
        offcanvasPanel);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return SimulationListPage.class;
  }

  private class NomCellFragment extends Fragment {
    private static final long serialVersionUID = 1L;

    public NomCellFragment(
        String id, IModel<Site> siteModel, SimulationSiteOffcanvasPanel offcanvasPanel) {
      super(id, "nomCellFragment", SimulationListPage.this);

      add(
          new AjaxLink<Void>("offcanvasLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
              offcanvasPanel.onShow(target, siteModel.getObject());
            }
          }.add(new CoreLabel("nom", BindingModel.of(siteModel, Bindings.site().nom()))));
    }
  }

  private class RisqueCellFragment extends Fragment {
    private static final long serialVersionUID = 1L;

    public RisqueCellFragment(
        String id, IModel<Site> siteModel, IModel<SimulationSearchDto> simulationSearchDtoModel) {
      super(id, "risqueCellFragment", SimulationListPage.this);

      IModel<Risque> risqueModel =
          LoadableDetachableModel.of(
              () ->
                  siteControllerService.getRisqueBrut(
                      siteModel.getObject(), simulationSearchDtoModel.getObject()));

      add(new RisqueRatingDisplayPanel("risque", risqueModel).small());
    }
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(simulationSearchDtoModel);
  }
}
