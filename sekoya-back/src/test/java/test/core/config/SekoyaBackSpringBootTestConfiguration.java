package test.core.config;

import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sekoya.back.business.notification.service.EmptyNotificationContentDescriptorFactoryImpl;
import sekoya.back.business.notification.service.ISekoyaNotificationContentDescriptorFactory;
import sekoya.back.config.SekoyaBackBaseConfiguration;
import sekoya.back.config.SekoyaBackDifferenceConfiguration;
import sekoya.back.config.SekoyaBackFlywayConfiguration;
import sekoya.back.config.SekoyaBackJpaConfiguration;
import sekoya.back.config.SekoyaBackManifestConfiguration;
import sekoya.back.config.SekoyaBackNotificationConfiguration;
import sekoya.back.config.SekoyaBackPropertyRegistryConfiguration;
import sekoya.back.config.SekoyaBackReferenceDataConfiguration;
import sekoya.back.config.SekoyaBackSecurityConfiguration;
import sekoya.back.config.SekoyaBackTaskManagementConfiguration;
import test.core.TestBackPackage;

@Configuration
@Import({
  SekoyaBackBaseConfiguration.class,
  SekoyaBackManifestConfiguration.class,
  SekoyaBackJpaConfiguration.class,
  SekoyaBackPropertyRegistryConfiguration.class,
  SekoyaBackSecurityConfiguration.class,
  SekoyaBackFlywayConfiguration.class,
  SekoyaBackTaskManagementConfiguration.class,
  SekoyaBackNotificationConfiguration.class,
  SekoyaBackReferenceDataConfiguration.class,
  SekoyaBackDifferenceConfiguration.class,
  SekoyaBackTestBaseConfiguration.class,
  PsqlTestContainerConfiguration.class
})
@ComponentScan(basePackageClasses = TestBackPackage.class)
public class SekoyaBackSpringBootTestConfiguration {

  @Bean
  public IRendererService rendererService() {
    return new EmptyRendererServiceImpl();
  }

  @Bean
  public ISekoyaNotificationContentDescriptorFactory contentDescriptorFactory() {
    return new EmptyNotificationContentDescriptorFactoryImpl();
  }
}
