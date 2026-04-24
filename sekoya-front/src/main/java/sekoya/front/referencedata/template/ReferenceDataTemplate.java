package sekoya.front.referencedata.template;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_READ;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.referencedata.page.ReferenceDataPage;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_REFERENCE_DATA_READ)
public abstract class ReferenceDataTemplate extends MainTemplate {

  private static final long serialVersionUID = -5226976873952135450L;

  protected ReferenceDataTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.referenceData"), ReferenceDataPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ReferenceDataPage.class;
  }
}
