package sekoya.back.business.processus.search;

import org.bindgen.Bindable;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusPriorite;
import sekoya.back.business.processus.model.atomic.ProcessusThematique;
import sekoya.back.business.site.model.Site;

@Bindable
public class ProcessusSearchQueryData implements ISearchQueryData<Processus> {

  private Organisation organisation;

  private Site site;

  private ProcessusThematique thematique;

  private String nom;

  private ProcessusPriorite priorite;

  private EnabledFilter enabledFilter = EnabledFilter.ENABLED_ONLY;

  public Organisation getOrganisation() {
    return organisation;
  }

  public void setOrganisation(Organisation organisation) {
    this.organisation = organisation;
  }

  public Site getSite() {
    return site;
  }

  public void setSite(Site site) {
    this.site = site;
  }

  public ProcessusThematique getThematique() {
    return thematique;
  }

  public void setThematique(ProcessusThematique thematique) {
    this.thematique = thematique;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public ProcessusPriorite getPriorite() {
    return priorite;
  }

  public void setPriorite(ProcessusPriorite priorite) {
    this.priorite = priorite;
  }

  public EnabledFilter getEnabledFilter() {
    return enabledFilter;
  }

  public void setEnabledFilter(EnabledFilter enabledFilter) {
    this.enabledFilter = enabledFilter;
  }
}
