package sekoya.front.referencedata.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_READ;

import igloo.wicket.condition.Condition;
import org.apache.wicket.Component;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import sekoya.front.common.component.NavTabsPanel;
import sekoya.front.referencedata.component.CommuneListPanel;
import sekoya.front.referencedata.component.DepartementListPanel;
import sekoya.front.referencedata.component.RegionListPanel;
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
                new NavTabsPanel.SimpleTabFactory("commune", "business.commune") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component createContent(String wicketId) {
                    return new CommuneListPanel(wicketId);
                  }
                })
            .add(
                new NavTabsPanel.SimpleTabFactory("departement", "business.departement") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component createContent(String wicketId) {
                    return new DepartementListPanel(wicketId);
                  }
                })
            .add(
                new NavTabsPanel.SimpleTabFactory("region", "business.region") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component createContent(String wicketId) {
                    return new RegionListPanel(wicketId);
                  }
                }));
  }
}
