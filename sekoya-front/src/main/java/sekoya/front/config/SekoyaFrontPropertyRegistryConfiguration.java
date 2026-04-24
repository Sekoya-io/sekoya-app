package sekoya.front.config;

import static sekoya.front.property.SekoyaFrontPropertyIds.APPLICATION_THEME;
import static sekoya.front.property.SekoyaFrontPropertyIds.MAINTENANCE_URL;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

import org.iglooproject.spring.config.IPropertyRegistryConfiguration;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;
import sekoya.front.common.template.theme.SekoyaApplicationTheme;

@Configuration
public class SekoyaFrontPropertyRegistryConfiguration implements IPropertyRegistryConfiguration {

  @Override
  public void register(IPropertyRegistry registry) {
    registry.registerEnum(
        APPLICATION_THEME, SekoyaApplicationTheme.class, SekoyaApplicationTheme.ADVANCED);

    registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE, 20);
    registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION, 20);

    registry.registerString(MAINTENANCE_URL);
  }
}
