package sekoya.back.business.organisation.search;

import org.bindgen.Bindable;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;
import sekoya.back.business.organisation.model.Organisation;

@Bindable
public class OrganisationSearchQueryData implements ISearchQueryData<Organisation> {

  private String nom;

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }
}
