package sekoya.back.api.geocodage.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import java.util.List;
import sekoya.back.api.geocodage.bean.atomic.GeocodageGeometryType;
import sekoya.back.api.geocodage.deserializer.GeocodageGeometryTypeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodageGeometryBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonDeserialize(using = GeocodageGeometryTypeDeserializer.class)
  private GeocodageGeometryType type;

  private List<Double> coordinates;

  public GeocodageGeometryType getType() {
    return type;
  }

  public void setType(GeocodageGeometryType type) {
    this.type = type;
  }

  public List<Double> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<Double> coordinates) {
    this.coordinates = coordinates;
  }
}
