package sekoya.back.business.notification.service;

import org.iglooproject.spring.notification.service.INotificationUrlBuilderService;
import sekoya.back.business.user.model.User;

public interface ISekoyaNotificationUrlBuilderService extends INotificationUrlBuilderService {

  String getUserDescriptionUrl(User user);
}
