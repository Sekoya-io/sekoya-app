package sekoya.back.business.notification.service;

import org.iglooproject.jpa.exception.ServiceException;
import sekoya.back.business.user.model.User;

public interface INotificationService {

  void sendExampleNotification(User user) throws ServiceException;

  void sendUserPasswordRecoveryRequest(User user) throws ServiceException;
}
