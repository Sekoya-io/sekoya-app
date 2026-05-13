package sekoya.back.business.referencedata.search;

import org.bindgen.Bindable;
import sekoya.back.business.referencedata.model.Region;

@Bindable
public class RegionSearchQueryData extends AbstractReferenceDataSearchQueryData<Region> {

  private String term;

  private String codeInsee;

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
}
