package sekoya.back.business.organisation.service.business;

import java.util.Objects;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.organisation.dao.IOrganisationDao;
import sekoya.back.business.organisation.model.Organisation;

@Service
public class OrganisationServiceImpl extends GenericEntityServiceImpl<Long, Organisation>
    implements IOrganisationService {

  private final IOrganisationDao dao;
  private final IHistoryEventSummaryService historyEventSummaryService;

  public OrganisationServiceImpl(
      IOrganisationDao dao, @Lazy IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.dao = dao;
    this.historyEventSummaryService = historyEventSummaryService;
  }

  @Override
  protected void createEntity(Organisation organisation)
      throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(organisation.getCreation());
    historyEventSummaryService.refresh(organisation.getModification());

    super.createEntity(organisation);
  }

  @Override
  protected void updateEntity(Organisation organisation)
      throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(organisation.getModification());

    super.updateEntity(organisation);
  }

  @Override
  public void saveOrganisation(Organisation organisation)
      throws SecurityServiceException, ServiceException {
    Objects.requireNonNull(organisation);

    if (organisation.isNew()) {
      create(organisation);
    } else {
      update(organisation);
    }
  }

  @Override
  public Organisation getDefault() {
    return dao.getDefault();
  }

  @Override
  public Organisation getByNomCaseInsensitive(String nom) {
    if (!StringUtils.hasText(nom)) {
      return null;
    }
    return dao.getByNomCaseInsensitive(nom);
  }
}
