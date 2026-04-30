package sekoya.front.referencedata.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_READ;

import igloo.wicket.condition.Condition;
import org.apache.wicket.Component;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import sekoya.front.common.component.NavTabsPanel;
import sekoya.front.referencedata.component.CityListPanel;
import sekoya.front.referencedata.template.ReferenceDataTemplate;

public class ReferenceDataPage extends ReferenceDataTemplate {

  private static final long serialVersionUID = -4381694964311714573L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_REFERENCE_DATA_READ))
        .page(ReferenceDataPage.class);
  }

  public ReferenceDataPage(PageParameters parameters) {
    super(parameters);

    add(
        new NavTabsPanel("tabs")
            .add(
                new NavTabsPanel.SimpleTabFactory("city", "business.city") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component createContent(String wicketId) {
                    return new CityListPanel(wicketId);
                  }
                }));
  }
}
