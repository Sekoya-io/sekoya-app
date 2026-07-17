package sekoya.back.business.organisation.model;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Collections;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.model.comparator.SiteComparator;
import sekoya.back.business.site.predicate.SitePredicates;
import sekoya.back.business.user.model.UserOrganisation;
import sekoya.back.business.user.model.comparator.UserOrganisationComparator;

@Entity
@Bindable
@Cacheable
@Indexed
public class Organisation extends GenericEntity<Long, Organisation> {

  private static final long serialVersionUID = 1L;

  public static final String NOM = "nom";
  public static final String NOM_AUTOCOMPLETE = NOM + "Autocomplete";

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  @KeywordField(name = NOM, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
  @FullTextField(name = NOM_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String nom;

  @Basic private Integer chiffreAffaires;

  @OneToMany(mappedBy = "organisation", fetch = FetchType.LAZY)
  @SortComparator(SiteComparator.class)
  private final SortedSet<Site> sites = Sets.newTreeSet(SiteComparator.get());

  @OneToMany(mappedBy = "organisation", fetch = FetchType.LAZY)
  @SortComparator(UserOrganisationComparator.class)
  private final SortedSet<UserOrganisation> usersOrganisation =
      Sets.newTreeSet(UserOrganisationComparator.get());

  @Embedded private HistoryEventSummary creation;

  @Embedded private HistoryEventSummary modification;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public Integer getChiffreAffaires() {
    return chiffreAffaires;
  }

  public void setChiffreAffaires(Integer chiffreAffaires) {
    this.chiffreAffaires = chiffreAffaires;
  }

  public SortedSet<Site> getSites() {
    return Collections.unmodifiableSortedSet(sites);
  }

  public void setSites(SortedSet<Site> sites) {
    CollectionUtils.replaceAll(this.sites, sites);
  }

  public SortedSet<Site> getSiteEnabled() {
    return sites.stream()
        .filter(SitePredicates.enabled())
        .collect(ImmutableSortedSet.toImmutableSortedSet(SiteComparator.get()));
  }

  public SortedSet<UserOrganisation> getUsersOrganisation() {
    return Collections.unmodifiableSortedSet(usersOrganisation);
  }

  public void setUsersOrganisation(SortedSet<UserOrganisation> usersOrganisation) {
    CollectionUtils.replaceAll(this.usersOrganisation, usersOrganisation);
  }

  public HistoryEventSummary getCreation() {
    if (creation == null) {
      creation = new HistoryEventSummary();
    }
    return creation;
  }

  public void setCreation(HistoryEventSummary creation) {
    this.creation = creation;
  }

  public HistoryEventSummary getModification() {
    if (modification == null) {
      modification = new HistoryEventSummary();
    }
    return modification;
  }

  public void setModification(HistoryEventSummary modification) {
    this.modification = modification;
  }

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper().add("nom", getNom());
  }
}
