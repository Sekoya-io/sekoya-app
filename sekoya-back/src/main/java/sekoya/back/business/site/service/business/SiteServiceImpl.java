package sekoya.back.business.site.service.business;

import java.util.Objects;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.site.dao.ISiteDao;
import sekoya.back.business.site.model.Site;

@Service
public class SiteServiceImpl extends GenericEntityServiceImpl<Long, Site> implements ISiteService {

  private final ISiteDao dao;
  private final IHistoryEventSummaryService historyEventSummaryService;

  public SiteServiceImpl(ISiteDao dao, IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.dao = dao;
    this.historyEventSummaryService = historyEventSummaryService;
  }

  @Override
  protected void createEntity(Site entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getCreation());
    historyEventSummaryService.refresh(entity.getModification());
    super.createEntity(entity);
  }

  @Override
  protected void updateEntity(Site entity) throws ServiceException, SecurityServiceException {
    historyEventSummaryService.refresh(entity.getModification());
    super.updateEntity(entity);
  }

  @Override
  public void saveSite(Site site) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(site);
    if (site.isNew()) {
      create(site);
    } else {
      update(site);
    }
  }

  @Override
  public void enable(Site site) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(site);
    site.setEnabled(true);
    update(site);
  }

  @Override
  public void disable(Site site) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(site);
    site.setEnabled(false);
    update(site);
  }

  @Override
  public Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom) {
    Objects.requireNonNull(organisation);
    Objects.requireNonNull(nom);
    return dao.getByOrganisationAndNomCaseInsensitive(organisation, nom);
  }
}
