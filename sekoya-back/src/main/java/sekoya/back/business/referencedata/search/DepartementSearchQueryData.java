package sekoya.back.business.referencedata.search;

import org.bindgen.Bindable;
import sekoya.back.business.referencedata.model.Departement;
import sekoya.back.business.referencedata.model.Region;

@Bindable
public class DepartementSearchQueryData extends AbstractReferenceDataSearchQueryData<Departement> {

  private String term;

  private String codeInsee;

  private Region region;

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

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
}
