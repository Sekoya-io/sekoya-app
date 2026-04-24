package sekoya.back.business.history.service;

import org.iglooproject.jpa.more.business.history.service.AbstractHistoryEventSummaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.service.business.IUserService;

@Service
public class HistoryEventSummaryServiceImpl extends AbstractHistoryEventSummaryServiceImpl<User>
    implements IHistoryEventSummaryService {

  @Autowired private IUserService userService;

  @Override
  protected User getDefaultSubject() {
    return userService.getAuthenticatedUser();
  }
}
