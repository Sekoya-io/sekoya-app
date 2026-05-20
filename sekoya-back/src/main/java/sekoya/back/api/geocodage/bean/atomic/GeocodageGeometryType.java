package sekoya.back.api.geocodage.bean.atomic;

import java.util.Arrays;

public enum GeocodageGeometryType {
  POINT("Point"),
  MULTI_POLYGON("MultiPolygon"),
  LINE_STRING("LineString");

  private final String label;

  GeocodageGeometryType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static GeocodageGeometryType getByLabel(String label) {
    return Arrays.stream(values())
        .filter(t -> t.getLabel().contains(label))
        .findFirst()
        .orElseThrow();
  }
}
