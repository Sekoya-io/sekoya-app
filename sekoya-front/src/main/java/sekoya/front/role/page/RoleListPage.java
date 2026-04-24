package sekoya.front.role.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ROLE_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ROLE_WRITE;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import sekoya.back.util.binding.Bindings;
import sekoya.front.role.model.RoleDataProvider;
import sekoya.front.role.template.RoleTemplate;

public class RoleListPage extends RoleTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_ROLE_READ))
        .page(RoleListPage.class);
  }

  @SpringBean private IPropertyService propertyService;

  public RoleListPage(PageParameters parameters) {
    super(parameters);

    RoleDataProvider dataProvider = new RoleDataProvider();

    EnclosureContainer headerElementsSection = new EnclosureContainer("headerElementsSection");
    add(headerElementsSection.anyChildVisible());

    headerElementsSection.add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                RoleAddPage.linkDescriptor()
                    .link("add")
                    .add(Condition.permission(GLOBAL_ROLE_WRITE).thenShow())));

    add(
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(new ResourceModel("business.role.title"), Bindings.role().title())
            .withLink(RoleDetailPage.MAPPER)
            .withClass("cell-w-250")
            .bootstrapCard()
            .ajaxPagers()
            .count("role.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE)));
  }
}
