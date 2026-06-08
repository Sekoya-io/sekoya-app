package sekoya.front.organisation.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ORGANISATION_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ORGANISATION_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.ORGANISATION_WRITE;
import static sekoya.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.bootstrap.modal.OneParameterModalOpenAjaxAction;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.organisation.search.OrganisationSort;
import sekoya.back.business.user.service.controller.IUserControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.component.HistoryEventSummaryPanel;
import sekoya.front.common.renderer.ActionRenderers;
import sekoya.front.common.renderer.CommonRenderers;
import sekoya.front.organisation.component.OrganisationListSearchPanel;
import sekoya.front.organisation.model.OrganisationDataProvider;
import sekoya.front.organisation.popup.OrganisationSavePopup;
import sekoya.front.organisation.template.OrganisationTemplate;

public class OrganisationListPage extends OrganisationTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_ORGANISATION_READ))
        .page(OrganisationListPage.class);
  }

  @SpringBean private IUserControllerService userControllerService;

  @SpringBean private IPropertyService propertyService;

  public OrganisationListPage(PageParameters parameters) {
    super(parameters);

    OrganisationDataProvider dataProvider = new OrganisationDataProvider();

    OrganisationSavePopup savePopup = new OrganisationSavePopup("savePopup");
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
                            savePopup.setUpAdd(new Organisation());
                          }
                        })
                    .add(Condition.permission(GLOBAL_ORGANISATION_WRITE).thenShow())));

    DecoratedCoreDataTablePanel<Organisation, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(
                new ResourceModel("business.organisation.nom"), Bindings.organisation().nom())
            .withSort(OrganisationSort.NOM, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.organisation.chiffreAffaires"),
                Bindings.organisation().chiffreAffaires(),
                CommonRenderers.kiloEuros())
            .withClass("cell-w-100")
            .addColumn(
                new AbstractCoreColumn<Organisation, OrganisationSort>(Model.of()) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Organisation>> cellItem,
                      String componentId,
                      IModel<Organisation> rowModel) {
                    cellItem.add(
                        new HistoryEventSummaryPanel(
                            componentId,
                            BindingModel.of(rowModel, Bindings.organisation().creation()),
                            BindingModel.of(rowModel, Bindings.organisation().modification())));
                  }
                })
            .withClass("cell-w-80 cell-w-fit text-center")
            .addActionColumn()
            .addAction(
                ActionRenderers.edit(),
                new OneParameterModalOpenAjaxAction<IModel<Organisation>>(savePopup) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void onShow(
                      AjaxRequestTarget target, IModel<Organisation> organisationModel) {
                    super.onShow(target, organisationModel);
                    savePopup.setUpEdit(organisationModel.getObject());
                  }
                })
            .whenPermission(ORGANISATION_WRITE)
            .withClassOnElements(BTN_TABLE_ROW_ACTION)
            .end()
            .withClass("cell-w-actions-1x cell-w-fit")
            .bootstrapCard()
            .ajaxPagers()
            .count("organisation.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(new OrganisationListSearchPanel("search", dataProvider, results), results);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return OrganisationListPage.class;
  }
}
