package sekoya.back.business.history.service;

import java.time.Instant;
import java.util.List;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.history.service.AbstractHistoryLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sekoya.back.business.history.dao.IHistoryLogDao;
import sekoya.back.business.history.model.HistoryDifference;
import sekoya.back.business.history.model.HistoryLog;
import sekoya.back.business.history.model.atomic.HistoryLogEventType;
import sekoya.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.service.business.IUserService;

@Service
public class HistoryLogServiceImpl
    extends AbstractHistoryLogServiceImpl<
        HistoryLog, HistoryLogEventType, HistoryDifference, HistoryLogAdditionalInformationBean>
    implements IHistoryLogService {

  private static final Supplier2<HistoryDifference> HISTORY_DIFFERENCE_SUPPLIER =
      () -> new HistoryDifference();

  @Autowired private IUserService userService;

  @Autowired
  public HistoryLogServiceImpl(IHistoryLogDao dao) {
    super(dao);
  }

  @Override
  protected <T> HistoryLog newHistoryLog(
      Instant date,
      HistoryLogEventType eventType,
      List<HistoryDifference> differences,
      T mainObject,
      HistoryLogAdditionalInformationBean additionalInformation) {
    HistoryLog log = new HistoryLog(date, eventType, valueService.createHistoryValue(mainObject));

    User subject = userService.getAuthenticatedUser();
    log.setSubject(valueService.createHistoryValue(subject));

    if (additionalInformation != null) {
      setAdditionalInformation(log, additionalInformation);
    }

    return log;
  }

  @Override
  protected Supplier2<HistoryDifference> newHistoryDifferenceSupplier() {
    return HISTORY_DIFFERENCE_SUPPLIER;
  }
}
