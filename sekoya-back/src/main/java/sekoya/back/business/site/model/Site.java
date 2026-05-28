package sekoya.back.business.site.model;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Sets;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Collections;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SortComparator;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.type.SqlTypes;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import org.locationtech.jts.geom.Point;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.common.model.embeddable.Adresse;
import sekoya.back.business.donneeclimatique.model.PointGeographique;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.comparator.ProcessusComparator;
import sekoya.back.business.site.model.atomic.SiteTypologie;
import sekoya.back.business.site.model.embeddable.SiteLittoral;

@Entity
@Bindable
@Cacheable
@Indexed
public class Site extends GenericEntity<Long, Site> {

  private static final long serialVersionUID = 1L;

  public static final String ORGANISATION = "organisation";
  public static final String NOM = "nom";
  public static final String NOM_AUTOCOMPLETE = NOM + "Autocomplete";
  public static final String TYPOLOGIE = "typologie";
  public static final String ADRESSE = "adresse";
  public static final String ADRESSE_EMBEDDED = ADRESSE + "Embedded";
  public static final String ADRESSE_COMMUNE = ADRESSE_EMBEDDED + "." + Adresse.COMMUNE;
  public static final String ADRESSE_COMMUNE_LABEL_SORT =
      ADRESSE_EMBEDDED + "." + Adresse.COMMUNE_LABEL_SORT;
  public static final String ENABLED = "enabled";

  @Id @GeneratedValue private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @GenericField(
      name = ORGANISATION,
      valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  private Organisation organisation;

  @Basic(optional = false)
  @KeywordField(name = NOM, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
  @FullTextField(name = NOM_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String nom;

  @Basic private Integer chiffreAffaires;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @GenericField(name = TYPOLOGIE)
  private SiteTypologie typologie;

  @Embedded
  @IndexedEmbedded(
      name = ADRESSE_EMBEDDED,
      includePaths = {Adresse.COMMUNE, Adresse.COMMUNE_LABEL_SORT})
  private Adresse adresse;

  @JdbcTypeCode(SqlTypes.GEOGRAPHY)
  private Point localisation;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private PointGeographique pointGeographique;

  @Embedded private SiteLittoral littoral;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Risque risqueBrutRcp45Annee2035;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Risque risqueBrutRcp45Annee2055;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Risque risqueBrutRcp85Annee2035;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Risque risqueBrutRcp85Annee2055;

  @Basic(optional = false)
  @GenericField(name = ENABLED)
  private boolean enabled = true;

  @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
  @SortComparator(ProcessusComparator.class)
  private final SortedSet<Processus> processus = Sets.newTreeSet(ProcessusComparator.get());

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

  public Integer getChiffreAffaires() {
    return chiffreAffaires;
  }

  public void setChiffreAffaires(Integer chiffreAffaires) {
    this.chiffreAffaires = chiffreAffaires;
  }

  public SiteTypologie getTypologie() {
    return typologie;
  }

  public void setTypologie(SiteTypologie typologie) {
    this.typologie = typologie;
  }

  public Adresse getAdresse() {
    if (adresse == null) {
      adresse = new Adresse();
    }
    return adresse;
  }

  public void setAdresse(Adresse adresse) {
    this.adresse = adresse;
  }

  public Point getLocalisation() {
    return localisation;
  }

  public void setLocalisation(Point localisation) {
    this.localisation = localisation;
  }

  public PointGeographique getPointGeographique() {
    return pointGeographique;
  }

  public void setPointGeographique(PointGeographique pointGeographique) {
    this.pointGeographique = pointGeographique;
  }

  public SiteLittoral getLittoral() {
    if (littoral == null) {
      littoral = new SiteLittoral();
    }
    return littoral;
  }

  public void setLittoral(SiteLittoral littoral) {
    this.littoral = littoral;
  }

  public Risque getRisqueBrutRcp45Annee2035() {
    return risqueBrutRcp45Annee2035;
  }

  public void setRisqueBrutRcp45Annee2035(Risque risqueBrutRcp45Annee2035) {
    this.risqueBrutRcp45Annee2035 = risqueBrutRcp45Annee2035;
  }

  public Risque getRisqueBrutRcp45Annee2055() {
    return risqueBrutRcp45Annee2055;
  }

  public void setRisqueBrutRcp45Annee2055(Risque risqueBrutRcp45Annee2055) {
    this.risqueBrutRcp45Annee2055 = risqueBrutRcp45Annee2055;
  }

  public Risque getRisqueBrutRcp85Annee2035() {
    return risqueBrutRcp85Annee2035;
  }

  public void setRisqueBrutRcp85Annee2035(Risque risqueBrutRcp85Annee2035) {
    this.risqueBrutRcp85Annee2035 = risqueBrutRcp85Annee2035;
  }

  public Risque getRisqueBrutRcp85Annee2055() {
    return risqueBrutRcp85Annee2055;
  }

  public void setRisqueBrutRcp85Annee2055(Risque risqueBrutRcp85Annee2055) {
    this.risqueBrutRcp85Annee2055 = risqueBrutRcp85Annee2055;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public SortedSet<Processus> getProcessus() {
    return Collections.unmodifiableSortedSet(processus);
  }

  public void setProcessus(SortedSet<Processus> processus) {
    CollectionUtils.replaceAll(this.processus, processus);
  }

  public void addProcessus(Processus processus) {
    this.processus.add(processus);
  }

  public void removeProcessus(Processus processus) {
    this.processus.add(processus);
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
    return super.toStringHelper().add("organisation", getOrganisation()).add("nom", getNom());
  }
}
