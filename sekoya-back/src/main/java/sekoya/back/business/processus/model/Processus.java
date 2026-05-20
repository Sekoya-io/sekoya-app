package sekoya.back.business.processus.model;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Sets;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.Collections;
import java.util.Optional;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.comparator.AleaComparator;
import sekoya.back.business.processus.model.atomic.ProcessusPriorite;
import sekoya.back.business.processus.model.atomic.ProcessusThematique;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.site.model.Site;

@Entity
@Bindable
@Cacheable
@Indexed
public class Processus extends GenericEntity<Long, Processus> {

  private static final long serialVersionUID = 1L;

  public static final String SITE = "site";
  public static final String SITE_EMBEDDED = SITE + "Embedded";
  public static final String SITE_ORGANISATION = SITE_EMBEDDED + "." + Site.ORGANISATION;
  public static final String THEMATIQUE = "thematique";
  public static final String NOM = "nom";
  public static final String NOM_AUTOCOMPLETE = NOM + "Autocomplete";
  public static final String PRIORITE = "priorite";

  @Id @GeneratedValue private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @GenericField(name = SITE, valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  @IndexedEmbedded(name = SITE_EMBEDDED, includePaths = Site.ORGANISATION)
  private Site site;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private ProcessusType type;

  @Basic(optional = false)
  @KeywordField(name = NOM, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
  @FullTextField(name = NOM_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String nom;

  @Column(length = Length.LONG32)
  private String description;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @GenericField(name = PRIORITE, sortable = Sortable.YES)
  private ProcessusPriorite priorite;

  @Basic(optional = false)
  private boolean enabled = true;

  @OneToMany(mappedBy = "processus", fetch = FetchType.LAZY, orphanRemoval = true)
  @SortComparator(AleaComparator.class)
  private final SortedSet<Alea> aleas = Sets.newTreeSet(AleaComparator.get());

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

  public Site getSite() {
    return site;
  }

  public void setSite(Site site) {
    this.site = site;
  }

  public ProcessusType getType() {
    return type;
  }

  @Transient
  @IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "type")))
  @GenericField(name = THEMATIQUE, sortable = Sortable.YES)
  public ProcessusThematique getThematique() {
    return Optional.ofNullable(type).map(ProcessusType::getThematique).orElse(null);
  }

  public void setType(ProcessusType type) {
    this.type = type;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProcessusPriorite getPriorite() {
    return priorite;
  }

  public void setPriorite(ProcessusPriorite priorite) {
    this.priorite = priorite;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public SortedSet<Alea> getAleas() {
    return Collections.unmodifiableSortedSet(aleas);
  }

  public void setAleas(SortedSet<Alea> aleas) {
    CollectionUtils.replaceAll(this.aleas, aleas);
  }

  public void addAlea(Alea alea) {
    this.aleas.add(alea);
  }

  public void removeAlea(Alea alea) {
    this.aleas.remove(alea);
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
    return super.toStringHelper()
        .add("site", getSite())
        .add("nom", getNom())
        .add("type", getType());
  }
}
