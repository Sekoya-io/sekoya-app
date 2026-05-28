package sekoya.back.business.site.model.embeddable;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import org.bindgen.Bindable;

@Embeddable
@Bindable
public class SiteLittoral implements Serializable {

  private static final long serialVersionUID = 1L;

  @Basic(optional = false)
  private boolean localisationLittoral;

  @Basic(optional = false)
  private boolean zoneSubmersible;

  public boolean isLocalisationLittoral() {
    return localisationLittoral;
  }

  public void setLocalisation(boolean localisationLittoral) {
    this.localisationLittoral = localisationLittoral;
  }

  public boolean isZoneSubmersible() {
    return zoneSubmersible;
  }

  public void setZoneSubmersible(boolean zoneSubmersible) {
    this.zoneSubmersible = zoneSubmersible;
  }
}
