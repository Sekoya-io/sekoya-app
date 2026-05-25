package sekoya.front.processus.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_PROCESSUS_READ;
import static sekoya.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.search.ProcessusSort;
import sekoya.back.business.user.search.IUserSearchQuery;
import sekoya.back.util.binding.Bindings;
import sekoya.front.SekoyaSession;
import sekoya.front.common.renderer.ActionRenderers;
import sekoya.front.common.util.CssClassConstants;
import sekoya.front.processus.component.ProcessusListSearchPanel;
import sekoya.front.processus.model.ProcessusDataProvider;
import sekoya.front.processus.template.ProcessusTemplate;
import sekoya.front.site.page.SiteDetailPage;

public class ProcessusListPage extends ProcessusTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_PROCESSUS_READ))
        .page(ProcessusListPage.class);
  }

  @SpringBean private IUserSearchQuery userSearchQuery;

  @SpringBean private IPropertyService propertyService;

  public ProcessusListPage(PageParameters parameters) {
    super(parameters);

    ProcessusDataProvider dataProvider = new ProcessusDataProvider();
    dataProvider
        .getDataModel()
        .getObject()
        .setOrganisation(SekoyaSession.get().getOrganisationModel().getObject());
    dataProvider.getDataModel().getObject().setEnabledFilter(EnabledFilter.ENABLED_ONLY);

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(ProcessusAddPage.linkDescriptor().link("add")));

    DecoratedCoreDataTablePanel<Processus, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(new ResourceModel("business.processus.nom"), Bindings.processus().nom())
            .withLink(ProcessusDetailPage.MAPPER)
            .withSort(ProcessusSort.NOM, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.processus.type"), Bindings.processus().type())
            .withClass("cell-w-200")
            .withClass(CssClassConstants.CELL_DISPLAY_XL)
            .addLabelColumn(
                new ResourceModel("business.processus.thematique"),
                Bindings.processus().thematique())
            .withSort(ProcessusSort.THEMATIQUE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-200")
            .withClass(CssClassConstants.CELL_DISPLAY_XL)
            .addLabelColumn(
                new ResourceModel("business.processus.priorite"), Bindings.processus().priorite())
            .withSort(ProcessusSort.PRIORITE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addColumn(
                new AbstractCoreColumn<Processus, ProcessusSort>(
                    new ResourceModel("business.processus.site")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Processus>> cellItem,
                      String componentId,
                      IModel<Processus> rowModel) {
                    cellItem.add(new SiteCellFragment(componentId, rowModel));
                  }
                })
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.processus.aleas"), Bindings.processus().aleas().size())
            .withClass("cell-w-100")
            .withClass(CssClassConstants.CELL_DISPLAY_XL)
            .addActionColumn()
            .addLink(ActionRenderers.edit(), ProcessusEditPage.MAPPER)
            .hideIfInvalid()
            .withClassOnElements(BTN_TABLE_ROW_ACTION)
            .end()
            .withClass("cell-w-actions-1x cell-w-fit")
            .rows()
            .end()
            .bootstrapCard()
            .ajaxPagers()
            .count("processus.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(new ProcessusListSearchPanel("search", dataProvider, results), results);
  }

  private class SiteCellFragment extends Fragment {
    private static final long serialVersionUID = 1L;

    public SiteCellFragment(String id, IModel<Processus> processusModel) {
      super(id, "siteCellFragment", ProcessusListPage.this);

      add(
          SiteDetailPage.MAPPER
              .map(BindingModel.of(processusModel, Bindings.processus().site()))
              .link("siteLink")
              .add(
                  new CoreLabel(
                      "site", BindingModel.of(processusModel, Bindings.processus().site()))),
          new CoreLabel(
              "commune",
              BindingModel.of(processusModel, Bindings.processus().site().adresse().commune())));
    }
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return ProcessusListPage.class;
  }
}
