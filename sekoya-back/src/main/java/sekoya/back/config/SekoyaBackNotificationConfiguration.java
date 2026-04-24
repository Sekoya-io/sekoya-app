package sekoya.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class SekoyaBackNotificationConfiguration {

  @Bean
  public FreeMarkerConfigurationFactoryBean freemarkerMailConfiguration() {
    FreeMarkerConfigurationFactoryBean configuration = new FreeMarkerConfigurationFactoryBean();
    configuration.setTemplateLoaderPath("classpath:notification");
    return configuration;
  }
}
