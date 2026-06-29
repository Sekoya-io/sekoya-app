package sekoya.back.business.site.search;

import org.bindgen.Bindable;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.model.Region;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.model.atomic.SiteTypologie;

@Bindable
public class SiteSearchQueryData implements ISearchQueryData<Site> {

  private Organisation organisation;

  private String nom;

  private SiteTypologie typologie;

  private Commune commune;

  private Region region;

  private EnabledFilter enabledFilter = EnabledFilter.ENABLED_ONLY;

  public Organisation getOrganisation() {
    return organisation;
  }

  public void setOrganisation(Organisation organisation) {
    this.organisation = organisation;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public SiteTypologie getTypologie() {
    return typologie;
  }

  public void setTypologie(SiteTypologie typologie) {
    this.typologie = typologie;
  }

  public Commune getCommune() {
    return commune;
  }

  public void setCommune(Commune commune) {
    this.commune = commune;
  }

  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  public EnabledFilter getEnabledFilter() {
    return enabledFilter;
  }

  public void setEnabledFilter(EnabledFilter enabledFilter) {
    this.enabledFilter = enabledFilter;
  }
}
