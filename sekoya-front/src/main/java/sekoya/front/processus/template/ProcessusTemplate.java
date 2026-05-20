package sekoya.front.processus.template;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_PROCESSUS_READ;

import com.google.common.base.Preconditions;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;
import sekoya.front.SekoyaSession;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.processus.page.ProcessusListPage;
import sekoya.front.site.page.SiteListPage;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_PROCESSUS_READ)
public abstract class ProcessusTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  protected ProcessusTemplate(PageParameters parameters) {
    Preconditions.checkState(SekoyaSession.get().hasOrganisation());
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.processus"), SiteListPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ProcessusListPage.class;
  }
}
