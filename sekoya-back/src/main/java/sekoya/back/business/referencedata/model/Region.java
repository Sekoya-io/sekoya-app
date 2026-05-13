package sekoya.back.business.referencedata.model;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.SortedSet;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;

@Entity
@Bindable
@Indexed
@Cacheable
public class Region extends ReferenceData<Region> {

  private static final long serialVersionUID = 1L;

  public static final String CODE_INSEE = "codeInsee";
  public static final String CODE_INSEE_AUTOCOMPLETE = CODE_INSEE + "Autocomplete";

  @Basic(optional = false)
  @Column(unique = true, length = Length.DEFAULT)
  @KeywordField(name = CODE_INSEE, sortable = Sortable.YES)
  @FullTextField(name = CODE_INSEE_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  private String codeInsee;

  // DO NOT USE - Hibernate Search association inverse side.
  @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
  private SortedSet<Departement> departements;

  @Embedded private HistoryEventSummary creation;

  @Embedded private HistoryEventSummary modification;

  public String getCodeInsee() {
    return codeInsee;
  }

  public void setCodeInsee(String codeInsee) {
    this.codeInsee = codeInsee;
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
