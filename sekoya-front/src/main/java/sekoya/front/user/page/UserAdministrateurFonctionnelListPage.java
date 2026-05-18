package sekoya.front.user.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_USER_READ;
import static sekoya.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static sekoya.front.common.util.CssClassConstants.CELL_DISPLAY_2XL;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.link.EmailLink;
import igloo.wicket.model.BindingModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.excel.AbstractExcelExportAjaxLink;
import org.iglooproject.wicket.more.excel.ExcelExportWorkInProgressModalPopupPanel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.business.user.predicate.UserPredicates;
import sekoya.back.business.user.search.UserSort;
import sekoya.back.business.user.service.controller.IUserControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.user.component.UserAdministrateurFonctionnelListSearchPanel;
import sekoya.front.user.export.UserAdministrateurFonctionnelExcelTableExport;
import sekoya.front.user.model.UserDataProvider;
import sekoya.front.user.popup.UserAdministrateurFonctionnelSavePopup;
import sekoya.front.user.renderer.UserEnabledRenderer;
import sekoya.front.user.template.UserTemplate;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_USER_READ)
public class UserAdministrateurFonctionnelListPage extends UserTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_USER_READ))
        .page(UserAdministrateurFonctionnelListPage.class);
  }

  @SpringBean private IPropertyService propertyService;

  @SpringBean private IUserControllerService userControllerService;

  public UserAdministrateurFonctionnelListPage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.administration.userAdministrateurFonctionnel")));

    UserDataProvider dataProvider = new UserDataProvider();
    dataProvider.getDataModel().getObject().setType(UserType.ADMINISTRATEUR_FONCTIONNEL);

    UserAdministrateurFonctionnelSavePopup addPopup =
        new UserAdministrateurFonctionnelSavePopup("addPopup");
    add(addPopup);

    ExcelExportWorkInProgressModalPopupPanel loadingPopup =
        new ExcelExportWorkInProgressModalPopupPanel("loadingPopup");
    add(loadingPopup);

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new AbstractExcelExportAjaxLink("exportExcel", loadingPopup, "export-users-") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected Workbook generateWorkbook() {
                    UserAdministrateurFonctionnelExcelTableExport export = new UserAdministrateurFonctionnelExcelTableExport(this);
                    return export.generate(dataProvider);
                  }
                },
                new BlankLink("add")
                    .add(
                        new AjaxModalOpenBehavior(addPopup, MouseEvent.CLICK) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onShow(AjaxRequestTarget target) {
                            addPopup.setUpAdd(new User());
                          }
                        })));

    DecoratedCoreDataTablePanel<User, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addBootstrapBadgeColumn(Model.of(), Bindings.user(), UserEnabledRenderer.get())
            .badgePill()
            .withClass("cell-w-100 text-center")
            .addLabelColumn(new ResourceModel("business.user.username"), Bindings.user().username())
            .withLink(
                UserAdministrateurFonctionnelDetailPage.MAPPER.setParameter2(new PageModel<>(this)))
            .withClass("cell-w-250")
            .addLabelColumn(new ResourceModel("business.user.lastName"), Bindings.user().lastName())
            .withSort(UserSort.LAST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.user.firstName"), Bindings.user().firstName())
            .withSort(UserSort.FIRST_NAME, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addColumn(
                new AbstractCoreColumn<>(new ResourceModel("business.user.emailAddress")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<User>> cellItem,
                      String componentId,
                      IModel<User> rowModel) {
                    IModel<String> emailAddressValueModel =
                        BindingModel.of(rowModel, Bindings.user().emailAddress().value());
                    cellItem.add(
                        new EmailLink(componentId, emailAddressValueModel) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onComponentTag(ComponentTag tag) {
                            tag.setName("a");
                            super.onComponentTag(tag);
                          }
                        }.add(
                            Condition.predicate(emailAddressValueModel, Predicates2.hasText())
                                .thenShow()));
                  }
                })
            .withClass("cell-w-350")
            .withClass(CELL_DISPLAY_2XL)
            .rows()
            .withClass(
                itemModel ->
                    Condition.predicate(itemModel, UserPredicates.disabled())
                        .then(BTN_TABLE_ROW_ACTION)
                        .otherwise(""))
            .end()
            .bootstrapCard()
            .ajaxPagers()
            .count("user.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(new UserAdministrateurFonctionnelListSearchPanel("search", dataProvider, results), results);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return UserAdministrateurFonctionnelListPage.class;
  }
}
