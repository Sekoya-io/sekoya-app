package sekoya.back.business.referencedata.search;

import org.bindgen.Bindable;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.model.Departement;
import sekoya.back.business.referencedata.model.Region;
import sekoya.back.business.referencedata.model.atomic.CommuneTypeInsee;

@Bindable
public class CommuneSearchQueryData extends AbstractReferenceDataSearchQueryData<Commune> {

  private String term;

  private String codePostal;

  private String codeInsee;

  private CommuneTypeInsee typeInsee;

  private Departement departement;

  private Region region;

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public String getCodePostal() {
    return codePostal;
  }

  public void setCodePostal(String codePostal) {
    this.codePostal = codePostal;
  }

  public String getCodeInsee() {
    return codeInsee;
  }

  public void setCodeInsee(String codeInsee) {
    this.codeInsee = codeInsee;
  }

  public CommuneTypeInsee getTypeInsee() {
    return typeInsee;
  }

  public void setTypeInsee(CommuneTypeInsee typeInsee) {
    this.typeInsee = typeInsee;
  }

  public Departement getDepartement() {
    return departement;
  }

  public void setDepartement(Departement departement) {
    this.departement = departement;
  }

  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }
}
