package sekoya.back.business.common.model.embeddable;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import sekoya.back.business.common.model.CodePostal;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.hibernate.type.CodePostalType;

@Embeddable
@Bindable
public class Adresse implements Serializable {

  private static final long serialVersionUID = 1L;

  @Basic(optional = false)
  private String adresse1;

  @Basic private String adresse2;

  @Basic(optional = false)
  @Type(CodePostalType.class)
  private CodePostal codePostal;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
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
