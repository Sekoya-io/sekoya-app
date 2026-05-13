package sekoya.back.business.processus.service;

import java.util.Objects;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.processus.dao.IProcessusDao;
import sekoya.back.business.processus.model.Processus;

@Service
public class ProcessusServiceImpl extends GenericEntityServiceImpl<Long, Processus>
    implements IProcessusService {

  private final IHistoryEventSummaryService historyEventSummaryService;

  public ProcessusServiceImpl(
      IProcessusDao dao, IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.historyEventSummaryService = historyEventSummaryService;
  }

  @Override
  protected void createEntity(Processus entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getCreation());
    historyEventSummaryService.refresh(entity.getModification());
    super.createEntity(entity);
  }

  @Override
  protected void updateEntity(Processus entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getModification());
    super.updateEntity(entity);
  }

  @Override
  public void saveProcessus(Processus processus) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(processus);
    if (processus.isNew()) {
      create(processus);
    } else {
      update(processus);
    }
  }
}
