package sekoya.front.organisation.template;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ORGANISATION_READ;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.organisation.page.OrganisationListPage;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_ORGANISATION_READ)
public abstract class OrganisationTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  protected OrganisationTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.organisation"), OrganisationListPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return OrganisationListPage.class;
  }
}
