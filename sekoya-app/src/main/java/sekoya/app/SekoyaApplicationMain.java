package sekoya.app;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import sekoya.app.config.SekoyaApplicationMainConfiguration;

@SpringBootApplication
@Import(SekoyaApplicationMainConfiguration.class)
public class SekoyaApplicationMain extends SpringBootServletInitializer {

  static void main(String[] args) {
    doConfigure(new SpringApplicationBuilder()).run(args);
  }

  public static SpringApplicationBuilder doConfigure(SpringApplicationBuilder application) {
    return application
        .sources(SekoyaApplicationMain.class)
        .initializers(new ExtendedApplicationContextInitializer());
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return SekoyaApplicationMain.doConfigure(application);
  }
}
