package sekoya.front.common.template.theme.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;
import sekoya.front.common.template.theme.SekoyaApplicationTheme;
import sekoya.front.navigation.page.HomePage;
import sekoya.front.property.SekoyaFrontPropertyIds;

public class ChangeApplicationThemeAjaxLink extends AjaxLink<Void> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IPropertyService propertyService;

  public ChangeApplicationThemeAjaxLink(String id) {
    super(id);
  }

  @Override
  public void onClick(AjaxRequestTarget target) {
    try {
      SekoyaApplicationTheme applicationTheme =
          propertyService.get(SekoyaFrontPropertyIds.APPLICATION_THEME);

      if (applicationTheme == null) {
        return;
      }

      propertyService.set(SekoyaFrontPropertyIds.APPLICATION_THEME, applicationTheme.next());

      throw HomePage.linkDescriptor().newRestartResponseException();
    } catch (Exception e) {
      throw new IllegalStateException("Error on updating application theme.", e);
    }
  }
}
