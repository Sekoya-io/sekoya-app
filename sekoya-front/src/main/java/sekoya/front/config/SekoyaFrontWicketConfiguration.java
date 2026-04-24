package sekoya.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import sekoya.front.SekoyaApplication;

@Configuration
@EnableWebSecurity
@Import(SekoyaFrontWicketFilterConfiguration.class)
public class SekoyaFrontWicketConfiguration {

  @Bean(name = {"SekoyaApplication", "application"})
  public SekoyaApplication application() {
    return new SekoyaApplication();
  }
}
