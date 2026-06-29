package sekoya.back.business.site.service.controller;

import java.util.List;
import java.util.SortedSet;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
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
  public Risque getRisqueBrut(Site site, SimulationSearchDto simulationSearchDto) {
    return siteService.getRisqueBrut(site, simulationSearchDto);
  }

  @Override
  public SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographique(
      Site site, SimulationSearchDto simulationSearchDto) {
    return siteService.listAleaRisqueGeographique(site, simulationSearchDto);
  }

  @Override
  public Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom) {
    return siteService.getByOrganisationAndNomCaseInsensitive(organisation, nom);
  }

  @Override
  public List<Site> listByOrganisation(Organisation organisation) {
    return siteService.listByOrganisation(organisation);
  }
}
