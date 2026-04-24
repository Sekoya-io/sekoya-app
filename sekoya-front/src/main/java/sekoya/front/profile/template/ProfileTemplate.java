package sekoya.front.profile.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.profile.page.ProfilePage;

public abstract class ProfileTemplate extends MainTemplate {

  private static final long serialVersionUID = 1029271113953538262L;

  protected ProfileTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.profile"), ProfilePage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ProfilePage.class;
  }
}
