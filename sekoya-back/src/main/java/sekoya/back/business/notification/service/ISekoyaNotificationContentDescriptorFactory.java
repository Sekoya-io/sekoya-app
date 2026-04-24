package sekoya.back.business.notification.service;

import java.time.Instant;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import sekoya.back.business.user.model.User;

public interface ISekoyaNotificationContentDescriptorFactory {

  INotificationContentDescriptor example(User user, Instant instant);

  INotificationContentDescriptor userPasswordRecoveryRequest(User user, Instant instant);
}
