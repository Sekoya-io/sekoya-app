package sekoya.back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import sekoya.back.scheduling.service.ISchedulingService;

@Configuration
@EnableScheduling
public class SekoyaBackSchedulingConfiguration {

  @Autowired private ISchedulingService schedulingService;

  @Scheduled(cron = "${tmp.clean.cron}")
  public void temporaryFilesCleaning() {
    schedulingService.temporaryFilesCleaning();
  }
}
