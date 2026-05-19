package sekoya.back.business.site.service.controller;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.service.business.ISiteService;

@Service
public class SiteControllerServiceImpl implements ISiteControllerService {

  private final ISiteService siteService;

  public SiteControllerServiceImpl(ISiteService siteService) {
    this.siteService = siteService;
  }

  @Override
  public void saveSite(Site site) throws SecurityServiceException, ServiceException {
    siteService.saveSite(site);
  }

  @Override
  public void enable(Site site) throws ServiceException, SecurityServiceException {
    siteService.enable(site);
  }

  @Override
  public void disable(Site site) throws ServiceException, SecurityServiceException {
    siteService.disable(site);
  }

  @Override
  public Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom) {
    return siteService.getByOrganisationAndNomCaseInsensitive(organisation, nom);
  }
}
