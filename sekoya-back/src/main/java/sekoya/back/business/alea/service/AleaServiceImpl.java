package sekoya.back.business.alea.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.dao.IAleaDao;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.history.service.IHistoryEventSummaryService;

@Service
public class AleaServiceImpl extends GenericEntityServiceImpl<Long, Alea> implements IAleaService {

  private final IHistoryEventSummaryService historyEventSummaryService;

  public AleaServiceImpl(IAleaDao dao, IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.historyEventSummaryService = historyEventSummaryService;
  }

  @Override
  protected void createEntity(Alea entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getCreation());
    historyEventSummaryService.refresh(entity.getModification());
    super.createEntity(entity);
  }

  @Override
  protected void updateEntity(Alea entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getModification());
    super.updateEntity(entity);
  }
}
