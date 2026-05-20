package sekoya.back.api.geocodage.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodageResponseBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<GeocodageGeocodeResponseBean> features;

  private String query;

  public List<GeocodageGeocodeResponseBean> getFeatures() {
    return features;
  }

  public void setFeatures(List<GeocodageGeocodeResponseBean> features) {
    this.features = features;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}
