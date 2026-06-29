package sekoya.back.business.common.model.embeddable;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import sekoya.back.business.common.model.CodePostal;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.hibernate.type.CodePostalType;

@Embeddable
@Bindable
public class Adresse implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String COMMUNE = "commune";
  public static final String COMMUNE_EMBEDDED = COMMUNE + "Embedded";
  public static final String COMMUNE_LABEL_SORT = COMMUNE_EMBEDDED + "." + Commune.LABEL_SORT;
  public static final String COMMUNE_DEPARTEMENT_REGION =
      COMMUNE_EMBEDDED + "." + Commune.DEPARTEMENT_REGION;

  @Basic(optional = false)
  private String adresse1;

  @Basic private String adresse2;

  @Basic(optional = false)
  @Type(CodePostalType.class)
  private CodePostal codePostal;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @GenericField(name = COMMUNE, valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  @IndexedEmbedded(
      name = COMMUNE_EMBEDDED,
      includePaths = {Commune.LABEL_SORT, Commune.DEPARTEMENT_REGION})
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private Commune commune;

  public String getAdresse1() {
    return adresse1;
  }

  public void setAdresse1(String adresse1) {
    this.adresse1 = adresse1;
  }

  public String getAdresse2() {
    return adresse2;
  }

  public void setAdresse2(String adresse2) {
    this.adresse2 = adresse2;
  }

  public CodePostal getCodePostal() {
    return codePostal;
  }

  public void setCodePostal(CodePostal codePostal) {
    this.codePostal = codePostal;
  }

  public Commune getCommune() {
    return commune;
  }

  public void setCommune(Commune commune) {
    this.commune = commune;
  }
}
