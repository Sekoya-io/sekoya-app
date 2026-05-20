package sekoya.back.business.processus.service.controller;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.processus.service.IProcessusService;
import sekoya.back.business.site.model.Site;

@Service
public class ProcessusControllerServiceImpl implements IProcessusControllerService {

  private final IProcessusService processusService;

  public ProcessusControllerServiceImpl(IProcessusService processusService) {
    this.processusService = processusService;
  }

  @Override
  public void saveProcessus(Processus processus) throws SecurityServiceException, ServiceException {
    processusService.saveProcessus(processus);
  }

  @Override
  public void enable(Processus processus) throws ServiceException, SecurityServiceException {
    processusService.enable(processus);
  }

  @Override
  public void disable(Processus processus) throws ServiceException, SecurityServiceException {
    processusService.disable(processus);
  }

  @Override
  public Processus getBySiteAndType(Site site, ProcessusType type) {
    return processusService.getBySiteAndType(site, type);
  }
}
