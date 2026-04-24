package sekoya.back.business.notification.service;

import sekoya.back.business.user.model.User;

public class EmptyNotificationUrlBuilderServiceImpl
    implements ISekoyaNotificationUrlBuilderService {

  @Override
  public String getUserDescriptionUrl(User user) {
    return null;
  }
}
