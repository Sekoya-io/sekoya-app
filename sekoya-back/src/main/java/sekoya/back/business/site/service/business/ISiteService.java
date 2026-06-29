package sekoya.back.business.site.service.business;

import java.util.List;
import java.util.SortedSet;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.javatuples.Pair;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;

public interface ISiteService extends IGenericEntityService<Long, Site> {

  void saveSite(Site site) throws ServiceException, SecurityServiceException;

  void refreshPointGeographique(Site site) throws ServiceException, SecurityServiceException;

  void refreshRisqueBrut(Site site) throws ServiceException, SecurityServiceException;

  void refreshRisqueBrut(Site site, Processus processus)
      throws ServiceException, SecurityServiceException;

  Risque getRisqueBrut(Site site, SimulationSearchDto simulationSearchDto);

  SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographique(
      Site site, SimulationSearchDto simulationSearchDto);

  void enable(Site site) throws ServiceException, SecurityServiceException;

  void disable(Site site) throws ServiceException, SecurityServiceException;

  Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom);

  List<Site> listByOrganisation(Organisation organisation);
}
