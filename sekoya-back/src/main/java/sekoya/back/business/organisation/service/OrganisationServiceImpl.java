package sekoya.back.business.organisation.service;

import java.util.Objects;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.organisation.dao.IOrganisationDao;
import sekoya.back.business.organisation.model.Organisation;

@Service
public class OrganisationServiceImpl extends GenericEntityServiceImpl<Long, Organisation>
    implements IOrganisationService {

  private final IHistoryEventSummaryService historyEventSummaryService;

  public OrganisationServiceImpl(
      IOrganisationDao dao, IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.historyEventSummaryService = historyEventSummaryService;
  }

  @Override
  protected void createEntity(Organisation entity)
      throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getCreation());
    historyEventSummaryService.refresh(entity.getModification());
    super.createEntity(entity);
  }

  @Override
  protected void updateEntity(Organisation entity)
      throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getModification());
    super.updateEntity(entity);
  }

  @Override
  public void saveOrganisation(Organisation organisation)
      throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(organisation);
    if (organisation.isNew()) {
      create(organisation);
    } else {
      update(organisation);
    }
  }
}
