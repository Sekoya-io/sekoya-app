package sekoya.front.site.template;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_SITE_READ;

import com.google.common.base.Preconditions;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;
import sekoya.front.SekoyaSession;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.site.page.SiteListPage;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_SITE_READ)
public abstract class SiteTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  protected SiteTemplate(PageParameters parameters) {
    Preconditions.checkState(SekoyaSession.get().hasOrganisation());
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(new ResourceModel("navigation.site"), SiteListPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return SiteListPage.class;
  }
}
