package sekoya.front.navigation.template;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import sekoya.front.common.template.MainTemplate;
import sekoya.front.navigation.page.HomePage;

public abstract class HomeTemplate extends MainTemplate {

  private static final long serialVersionUID = 1L;

  protected HomeTemplate(PageParameters parameters) {
    super(parameters);

    getBodyElement().add(new ClassAttributeAppender(Model.of("home sidebar-expand-0")));
  }

  @Override
  protected boolean isHomePage() {
    return true;
  }

  @Override
  protected Condition displayNavbar() {
    return Condition.alwaysFalse();
  }

  @Override
  protected Condition displayBreadcrumb() {
    return Condition.alwaysFalse();
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return HomePage.class;
  }
}
