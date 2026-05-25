package sekoya.back.business.alea.search;

import org.bindgen.Bindable;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaSensibilite;
import sekoya.back.business.processus.model.Processus;

@Bindable
public class AleaSearchQueryData implements ISearchQueryData<Alea> {

  private Processus processus;

  private AleaSensibilite sensibilite;

  public Processus getProcessus() {
    return processus;
  }

  public void setProcessus(Processus processus) {
    this.processus = processus;
  }

  public AleaSensibilite getSensibilite() {
    return sensibilite;
  }

  public void setSensibilite(AleaSensibilite sensibilite) {
    this.sensibilite = sensibilite;
  }
}
