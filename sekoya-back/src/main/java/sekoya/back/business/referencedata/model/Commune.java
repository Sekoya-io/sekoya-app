package sekoya.back.business.referencedata.model;

import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.Type;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import sekoya.back.business.common.model.CodePostal;
import sekoya.back.business.referencedata.model.atomic.CommuneTypeInsee;
import sekoya.back.hibernate.search.bridge.CodePostalValueBridge;
import sekoya.back.hibernate.type.CodePostalType;

@Entity
@Bindable
@Indexed
@Cacheable
@Table(
    check = {
      @CheckConstraint(
          name = "commune_departement_check",
          constraint =
              "departement_id IS NOT NULL OR typeInsee IN ('COMMUNE_DELEGUEE', 'COMMUNE_ASSOCIEE')"),
      @CheckConstraint(
          name = "commune_parent_check",
          constraint =
              """
                (typeInsee IN ('COMMUNE', 'COMMUNE_COMER') AND parent_id IS NULL)
                  OR (typeInsee IN ('ARRONDISSEMENT_MUNICIPAL', 'COMMUNE_DELEGUEE', 'COMMUNE_ASSOCIEE') AND parent_id IS NOT NULL)
                """)
    })
public class Commune extends ReferenceData<Commune> {

  private static final long serialVersionUID = 1L;

  public static final String CODES_POSTAUX = "codesPostaux";
  public static final String CODES_POSTAUX_AUTOCOMPLETE = CODES_POSTAUX + "Autocomplete";
  public static final String CODE_INSEE = "codeInsee";
  public static final String CODE_INSEE_AUTOCOMPLETE = CODE_INSEE + "Autocomplete";
  public static final String TYPE_INSEE = "typeInsee";
  public static final String DEPARTEMENT = "departement";
  public static final String DEPARTEMENT_EMBEDDED = DEPARTEMENT + "Embedded";
  public static final String DEPARTEMENT_REGION = DEPARTEMENT_EMBEDDED + "." + Departement.REGION;
  public static final String DEPARTEMENT_LABEL_SORT =
      DEPARTEMENT_EMBEDDED + "." + Departement.LABEL_SORT;
  public static final String DEPARTEMENT_REGION_LABEL_SORT =
      DEPARTEMENT_EMBEDDED + "." + Departement.REGION_LABEL_SORT;

  @ElementCollection
  @CollectionTable(
      name = "commune_codepostal",
      indexes = {
        @Index(name = "commune_codepostal_commune_id_idx", columnList = "commune_id"),
        @Index(name = "commune_codepostal_codepostal_idx", columnList = "codepostal")
      })
  @Column(name = "codepostal")
  @Type(CodePostalType.class)
  @SortNatural
  @GenericField(
      name = CODES_POSTAUX,
      valueBridge = @ValueBridgeRef(type = CodePostalValueBridge.class),
      sortable = Sortable.YES)
  @FullTextField(
      name = CODES_POSTAUX_AUTOCOMPLETE,
      valueBridge = @ValueBridgeRef(type = CodePostalValueBridge.class),
      analyzer = HibernateSearchAnalyzer.TEXT)
  private SortedSet<CodePostal> codesPostaux = Sets.newTreeSet(Ordering.natural().nullsLast());

  @Basic(optional = false)
  @Column(length = 5)
  @GenericField(name = CODE_INSEE, sortable = Sortable.YES)
  @FullTextField(name = CODE_INSEE_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String codeInsee;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @GenericField(name = TYPE_INSEE)
  private CommuneTypeInsee typeInsee;

  @ManyToOne(fetch = FetchType.LAZY)
  @GenericField(
      name = DEPARTEMENT,
      valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  @IndexedEmbedded(
      name = DEPARTEMENT_EMBEDDED,
      includePaths = {Departement.LABEL_SORT, Departement.REGION, Departement.REGION_LABEL_SORT})
  private Departement departement;

  @ManyToOne(fetch = FetchType.LAZY)
  private Commune parent;

  @Embedded private HistoryEventSummary creation;

  @Embedded private HistoryEventSummary modification;

  public CommuneTypeInsee getTypeInsee() {
    return typeInsee;
  }

  public void setTypeInsee(CommuneTypeInsee typeInsee) {
    this.typeInsee = typeInsee;
  }

  public String getCodeInsee() {
    return codeInsee;
  }

  public void setCodeInsee(String codeInsee) {
    this.codeInsee = codeInsee;
  }

  public SortedSet<CodePostal> getCodesPostaux() {
    return codesPostaux;
  }

  public void setCodesPostaux(SortedSet<CodePostal> codesPostaux) {
    this.codesPostaux = codesPostaux;
  }

  public Commune getParent() {
    return parent;
  }

  public void setParent(Commune parent) {
    this.parent = parent;
  }

  public Departement getDepartement() {
    return departement;
  }

  public void setDepartement(Departement departement) {
    this.departement = departement;
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
}
