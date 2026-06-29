package sekoya.back.business.site.service.controller;

import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.SITE_DISABLE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.SITE_ENABLE;
import static sekoya.back.security.model.SekoyaSecurityExpressionConstants.SITE_WRITE;

import java.util.List;
import java.util.SortedSet;
import org.iglooproject.commons.util.security.PermissionObject;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.javatuples.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;

public interface ISiteControllerService {

  @PreAuthorize(SITE_WRITE)
  void saveSite(@PermissionObject Site site) throws SecurityServiceException, ServiceException;

  @PreAuthorize(SITE_ENABLE)
  void enable(@PermissionObject Site site) throws ServiceException, SecurityServiceException;

  @PreAuthorize(SITE_DISABLE)
  void disable(@PermissionObject Site site) throws ServiceException, SecurityServiceException;

  Risque getRisqueBrut(Site site, SimulationSearchDto simulationSearchDto);

  SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographique(
      Site site, SimulationSearchDto simulationSearchDto);

  Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom);

  List<Site> listByOrganisation(Organisation organisation);
}
