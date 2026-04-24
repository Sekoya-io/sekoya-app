package sekoya.back.config;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import igloo.loginmdc.annotation.LogExecutionContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class SekoyaBackLoggerConfig {

  @Bean
  @Order(HIGHEST_PRECEDENCE)
  public LogExecutionContributor loginContributor() {
    return new LogExecutionContributor();
  }
}
