package sekoya.front.site.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_SITE_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_SITE_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_WRITE;
import static sekoya.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.bootstrap.modal.OneParameterModalOpenAjaxAction;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
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
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.search.SiteSort;
import sekoya.back.business.user.search.IUserSearchQuery;
import sekoya.back.util.binding.Bindings;
import sekoya.front.SekoyaSession;
import sekoya.front.common.renderer.ActionRenderers;
import sekoya.front.site.component.SiteListSearchPanel;
import sekoya.front.site.model.SiteDataProvider;
import sekoya.front.site.popup.SiteSavePopup;
import sekoya.front.site.template.SiteTemplate;

public class SiteListPage extends SiteTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_SITE_READ))
        .page(SiteListPage.class);
  }

  @SpringBean private IUserSearchQuery userSearchQuery;

  @SpringBean private IPropertyService propertyService;

  public SiteListPage(PageParameters parameters) {
    super(parameters);

    SiteDataProvider dataProvider = new SiteDataProvider();
    dataProvider
        .getDataModel()
        .getObject()
        .setOrganisation(SekoyaSession.get().getOrganisationModel().getObject());
    dataProvider.getDataModel().getObject().setEnabledFilter(EnabledFilter.ENABLED_ONLY);

    SiteSavePopup savePopup = new SiteSavePopup("savePopup");
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

    DecoratedCoreDataTablePanel<Site, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(new ResourceModel("business.site.nom"), Bindings.site().nom())
            .withLink(SiteDetailPage.MAPPER)
            .withSort(SiteSort.NOM, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.site.typologie"), Bindings.site().typologie())
            .withClass("cell-w-200")
            .addLabelColumn(new ResourceModel("business.site.adresse"), Bindings.site().adresse())
            .withSort(
                SiteSort.ADRESSE_COMMUNE_LABEL, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .multiline()
            .withClass("cell-w-300")
            .addActionColumn()
            .addAction(
                ActionRenderers.edit(),
                new OneParameterModalOpenAjaxAction<IModel<Site>>(savePopup) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void onShow(AjaxRequestTarget target, IModel<Site> siteModel) {
                    super.onShow(target, siteModel);
                    savePopup.setUpEdit(siteModel.getObject());
                  }
                })
            .whenPermission(SITE_WRITE)
            .withClassOnElements(BTN_TABLE_ROW_ACTION)
            .end()
            .withClass("cell-w-actions-1x cell-w-fit")
            .rows()
            .end()
            .bootstrapCard()
            .ajaxPagers()
            .count("site.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(new SiteListSearchPanel("search", dataProvider, results), results);
  }

  @Override
  protected Class<? extends WebPage> getSecondMenuPage() {
    return SiteListPage.class;
  }
}
