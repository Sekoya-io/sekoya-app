package sekoya.back.api.geocodage.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodageGeocodeResponseBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private GeocodageAddressPropertiesBean properties;

  private GeocodageGeometryBean geometry;

  public GeocodageAddressPropertiesBean getProperties() {
    return properties;
  }

  public void setProperties(GeocodageAddressPropertiesBean properties) {
    this.properties = properties;
  }

  public GeocodageGeometryBean getGeometry() {
    return geometry;
  }

  public void setGeometry(GeocodageGeometryBean geometry) {
    this.geometry = geometry;
  }
}
