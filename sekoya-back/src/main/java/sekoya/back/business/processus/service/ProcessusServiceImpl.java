package sekoya.back.business.processus.service;

import java.util.Objects;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.service.IAleaService;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.processus.dao.IProcessusDao;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.site.model.Site;

@Service
public class ProcessusServiceImpl extends GenericEntityServiceImpl<Long, Processus>
    implements IProcessusService {

  private final IProcessusDao dao;
  private final IAleaService aleaService;
  private final IHistoryEventSummaryService historyEventSummaryService;

  public ProcessusServiceImpl(
      IProcessusDao dao,
      IAleaService aleaService,
      IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.dao = dao;
    this.aleaService = aleaService;
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

    for (Alea alea : processus.getAleas()) {
      aleaService.saveAlea(alea);
    }
  }

  @Override
  public void enable(Processus processus) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(processus);
    processus.setEnabled(true);
    update(processus);
  }

  @Override
  public void disable(Processus processus) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(processus);
    processus.setEnabled(false);
    update(processus);
  }

  @Override
  public Processus getBySiteAndType(Site site, ProcessusType type) {
    if (site == null || type == null) {
      return null;
    }
    return dao.getBySiteAndType(site, type);
  }
}
