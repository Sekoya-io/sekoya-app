package sekoya.back.business.referencedata.model;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;

@Entity
@Bindable
@Indexed
@Cacheable
public class Departement extends ReferenceData<Departement> {

  private static final long serialVersionUID = 1L;

  public static final String CODE_INSEE = "codeInsee";
  public static final String CODE_INSEE_AUTOCOMPLETE = CODE_INSEE + "Autocomplete";
  public static final String REGION = "region";
  public static final String REGION_EMBEDDED = REGION + "Embedded";
  public static final String REGION_LABEL_SORT = REGION_EMBEDDED + "." + Region.LABEL_SORT;

  @Basic(optional = false)
  @Column(unique = true, length = Length.DEFAULT)
  @KeywordField(name = CODE_INSEE, sortable = Sortable.YES)
  @FullTextField(name = CODE_INSEE_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String codeInsee;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @GenericField(name = REGION, valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  @IndexedEmbedded(name = REGION_EMBEDDED, includePaths = Region.LABEL_SORT)
  private Region region;

  // DO NOT USE - Hibernate Search association inverse side.
  @OneToMany(mappedBy = "departement", fetch = FetchType.LAZY)
  private SortedSet<Commune> communes;

  @Embedded private HistoryEventSummary creation;

  @Embedded private HistoryEventSummary modification;

  public String getCodeInsee() {
    return codeInsee;
  }

  public void setCodeInsee(String codeInsee) {
    this.codeInsee = codeInsee;
  }

  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
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
