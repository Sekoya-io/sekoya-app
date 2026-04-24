package sekoya.back.config;

import org.apache.commons.lang3.EnumUtils;
import org.iglooproject.jpa.more.config.ImmutableTaskManagement.Builder;
import org.iglooproject.jpa.more.config.TaskManagementConfigurer;
import org.springframework.context.annotation.Configuration;
import sekoya.back.business.task.model.SekoyaTaskQueueId;

@Configuration
public class SekoyaBackTaskManagementConfiguration {

  @Configuration
  public static class ApplicationTaskManagementConfigurer implements TaskManagementConfigurer {
    @Override
    public void configure(Builder taskManagement) {
      taskManagement.addAllQueueIds(EnumUtils.getEnumList(SekoyaTaskQueueId.class));
    }
  }
}
