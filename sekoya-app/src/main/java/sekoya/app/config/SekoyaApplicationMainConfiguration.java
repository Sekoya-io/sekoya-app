package sekoya.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sekoya.back.config.SekoyaBackBaseConfiguration;
import sekoya.back.config.SekoyaBackDifferenceConfiguration;
import sekoya.back.config.SekoyaBackFlywayConfiguration;
import sekoya.back.config.SekoyaBackJpaConfiguration;
import sekoya.back.config.SekoyaBackLoggerConfig;
import sekoya.back.config.SekoyaBackManifestConfiguration;
import sekoya.back.config.SekoyaBackNotificationConfiguration;
import sekoya.back.config.SekoyaBackPropertyRegistryConfiguration;
import sekoya.back.config.SekoyaBackReferenceDataConfiguration;
import sekoya.back.config.SekoyaBackSchedulingConfiguration;
import sekoya.back.config.SekoyaBackSecurityConfiguration;
import sekoya.back.config.SekoyaBackTaskManagementConfiguration;
import sekoya.front.SekoyaApplication;
import sekoya.front.config.SekoyaFrontNotificationConfiguration;
import sekoya.front.config.SekoyaFrontPropertyRegistryConfiguration;
import sekoya.front.config.SekoyaFrontSecurityConfiguration;
import sekoya.front.config.SekoyaFrontServletConfiguration;
import sekoya.front.config.SekoyaFrontWicketConfiguration;

@Configuration
@Import({
  SekoyaBackBaseConfiguration.class,
  SekoyaBackManifestConfiguration.class,
  SekoyaBackJpaConfiguration.class,
  SekoyaBackPropertyRegistryConfiguration.class,
  SekoyaBackSecurityConfiguration.class,
  SekoyaBackFlywayConfiguration.class,
  SekoyaBackSchedulingConfiguration.class,
  SekoyaBackTaskManagementConfiguration.class,
  SekoyaBackNotificationConfiguration.class,
  SekoyaBackReferenceDataConfiguration.class,
  SekoyaBackDifferenceConfiguration.class,
  SekoyaFrontPropertyRegistryConfiguration.class,
  SekoyaFrontSecurityConfiguration.class,
  SekoyaFrontServletConfiguration.class,
  SekoyaFrontWicketConfiguration.class,
  SekoyaFrontNotificationConfiguration.class,
  SekoyaBackLoggerConfig.class
})
@ComponentScan(
    basePackageClasses = {SekoyaApplication.class},
    excludeFilters = @Filter(Configuration.class))
public class SekoyaApplicationMainConfiguration {}
