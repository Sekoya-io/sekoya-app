package sekoya.back.business.referencedata.model;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.bindgen.Bindable;
import org.hibernate.Length;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;

@MappedSuperclass
@Bindable
public class ReferenceData<E extends ReferenceData<?>> extends GenericReferenceData<E, String>
    implements IReferenceDataBindingInterface {

  private static final long serialVersionUID = -1779439527249543663L;

  public static final String LABEL_AUTOCOMPLETE = "labelAutocomplete";

  public static final String LABEL_SORT = "labelSort";

  @Basic(optional = false)
  @Column(length = Length.LONG)
  @FullTextField(name = LABEL_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
  @KeywordField(
      name = LABEL_SORT,
      normalizer = HibernateSearchNormalizer.TEXT,
      sortable = Sortable.YES)
  private String label;

  public ReferenceData() {
    this("");
  }

  public ReferenceData(String label) {
    setLabel(label);
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }
}
