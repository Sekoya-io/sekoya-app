package sekoya.back.business.site.service.business;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import sekoya.back.business.common.model.Latitude;
import sekoya.back.business.common.model.Longitude;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.donneeclimatique.service.IDonneeClimatiqueService;
import sekoya.back.business.history.service.IHistoryEventSummaryService;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.predicate.ProcessusPredicates;
import sekoya.back.business.processus.service.IProcessusService;
import sekoya.back.business.site.dao.ISiteDao;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;

@Service
public class SiteServiceImpl extends GenericEntityServiceImpl<Long, Site> implements ISiteService {

  private final ISiteDao dao;
  private final IProcessusService processusService;
  private final IDonneeClimatiqueService donneClimatiqueService;
  private final IHistoryEventSummaryService historyEventSummaryService;

  public SiteServiceImpl(
      ISiteDao dao,
      @Lazy IProcessusService processusService,
      IDonneeClimatiqueService donneClimatiqueService,
      IHistoryEventSummaryService historyEventSummaryService) {
    super(dao);
    this.dao = dao;
    this.processusService = processusService;
    this.donneClimatiqueService = donneClimatiqueService;
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

    refreshLatitudeLongitude(site);
    refreshPointGeographique(site);
    refreshRisqueBrut(site);

    if (site.isNew()) {
      create(site);
    } else {
      update(site);
    }
  }

  private void refreshLatitudeLongitude(Site site) {
    site.setLongitude(new Longitude(BigDecimal.valueOf(site.getLocalisation().getX())));
    site.setLatitude(new Latitude(BigDecimal.valueOf(site.getLocalisation().getY())));
  }

  @Override
  public void refreshPointGeographique(Site site)
      throws ServiceException, SecurityServiceException {
    site.setPointGeographique(dao.getPointGeographique(site));
  }

  @Override
  public void refreshRisqueBrut(Site site) throws ServiceException, SecurityServiceException {
    refreshRisqueBrut(
        site, site.getProcessus().stream().filter(ProcessusPredicates.enabled()).toList());
  }

  @Override
  public void refreshRisqueBrut(Site site, Processus processus)
      throws ServiceException, SecurityServiceException {
    refreshRisqueBrut(site, List.of(processus));
  }

  private void refreshRisqueBrut(Site site, Collection<Processus> processus)
      throws ServiceException, SecurityServiceException {
    for (Processus p : processus) {
      processusService.refreshRisqueBrut(p);
    }

    if (processus.isEmpty()) {
      for (var data :
          List.of(
              Triplet.with(
                  Bindings.site().risqueBrutRcp45Annee2035(), Scenario.RCP_4_5, Horizon.ANNEE_2035),
              Triplet.with(
                  Bindings.site().risqueBrutRcp45Annee2055(), Scenario.RCP_4_5, Horizon.ANNEE_2055),
              Triplet.with(
                  Bindings.site().risqueBrutRcp85Annee2035(), Scenario.RCP_8_5, Horizon.ANNEE_2035),
              Triplet.with(
                  Bindings.site().risqueBrutRcp85Annee2055(),
                  Scenario.RCP_8_5,
                  Horizon.ANNEE_2055))) {
        data.getValue0()
            .setWithRoot(
                site,
                donneClimatiqueService
                    .getPlusDefavorableByPointGeographique(
                        site.getPointGeographique(), data.getValue1(), data.getValue2())
                    .getEvolution()
                    .getRisque());
      }
    } else {
      for (var data :
          List.of(
              Pair.with(
                  Bindings.site().risqueBrutRcp45Annee2035(),
                  Bindings.processus().risqueBrutRcp45Annee2035()),
              Pair.with(
                  Bindings.site().risqueBrutRcp45Annee2055(),
                  Bindings.processus().risqueBrutRcp45Annee2055()),
              Pair.with(
                  Bindings.site().risqueBrutRcp85Annee2035(),
                  Bindings.processus().risqueBrutRcp85Annee2035()),
              Pair.with(
                  Bindings.site().risqueBrutRcp85Annee2055(),
                  Bindings.processus().risqueBrutRcp85Annee2055()))) {
        data.getValue0()
            .setWithRoot(
                site,
                site.getProcessus().stream()
                    .map(data.getValue1())
                    .filter(Objects::nonNull)
                    .max(Comparator.comparingInt(Risque::getScore))
                    .orElseThrow());
      }
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

  @Override
  public List<Site> listByOrganisation(Organisation organisation) {
    Objects.requireNonNull(organisation);
    return dao.listByOrganisation(organisation);
  }
}
