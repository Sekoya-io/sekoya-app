package sekoya.front.user.template;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_USER_READ;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.user.page.UserOrganisationListPage;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_USER_READ)
public abstract class UserTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  protected UserTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("navigation.administration")));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return UserOrganisationListPage.class;
  }
}
