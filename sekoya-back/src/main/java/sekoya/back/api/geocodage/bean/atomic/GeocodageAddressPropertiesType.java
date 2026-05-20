package sekoya.back.api.geocodage.bean.atomic;

import java.util.Arrays;

public enum GeocodageAddressPropertiesType {
  HOUSENUMBER("housenumber"),
  STREET("street"),
  LOCALITY("locality"),
  MUNICIPALITY("municipality");

  private final String label;

  GeocodageAddressPropertiesType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static GeocodageAddressPropertiesType getByLabel(String label) {
    return Arrays.stream(values())
        .filter(t -> t.getLabel().contains(label))
        .findFirst()
        .orElseThrow();
  }
}
