package sekoya.front.config;

import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sekoya.back.business.notification.service.ISekoyaNotificationContentDescriptorFactory;
import sekoya.back.business.notification.service.ISekoyaNotificationUrlBuilderService;
import sekoya.front.notification.service.SekoyaNotificationContentDescriptorFactoryImpl;
import sekoya.front.notification.service.SekoyaNotificationUrlBuilderServiceImpl;

@Configuration
public class SekoyaFrontNotificationConfiguration {

  @Bean
  public ISekoyaNotificationContentDescriptorFactory contentDescriptorFactory(
      IWicketContextProvider wicketContextProvider) {
    return new SekoyaNotificationContentDescriptorFactoryImpl(wicketContextProvider);
  }

  @Bean
  public ISekoyaNotificationUrlBuilderService notificationUrlBuilderService(
      IWicketContextProvider wicketContextProvider) {
    return new SekoyaNotificationUrlBuilderServiceImpl(wicketContextProvider);
  }
}
