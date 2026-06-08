package sekoya.front.common.map.model;

import java.io.Serializable;

public record MapConfig(String styleUrl, String tilesOverrideUrl, BoundingBox fallbackBounds)
    implements Serializable {

  public record BoundingBox(double swLng, double swLat, double neLng, double neLat)
      implements Serializable {}

  public static final String VERSATILES_COLORFUL =
      "https://tiles.versatiles.org/assets/styles/colorful/style.json";
  public static final String VERSATILES_GRAYBEARD =
      "https://tiles.versatiles.org/assets/styles/graybeard/style.json";
  public static final String VERSATILES_ECLIPSE =
      "https://tiles.versatiles.org/assets/styles/eclipse/style.json";
  public static final String VERSATILES_NEUTRINO =
      "https://tiles.versatiles.org/assets/styles/neutrino/style.json";

  public static final String OSM_SHORTBREAD =
      "https://vector.openstreetmap.org/shortbread_v1/{z}/{x}/{y}.mvt";

  public static final BoundingBox FRANCE_METROPOLITAN = new BoundingBox(-5.5, 41.3, 10.0, 51.5);
  public static final BoundingBox WESTERN_EUROPE = new BoundingBox(-10.0, 35.0, 20.0, 60.0);

  public static MapConfig defaults() {
    return new MapConfig(VERSATILES_COLORFUL, OSM_SHORTBREAD, FRANCE_METROPOLITAN);
  }

  public MapConfig withStyle(String styleUrl) {
    return new MapConfig(styleUrl, tilesOverrideUrl, fallbackBounds);
  }

  public MapConfig withTilesOverride(String tilesOverrideUrl) {
    return new MapConfig(styleUrl, tilesOverrideUrl, fallbackBounds);
  }

  public MapConfig withFallbackBounds(BoundingBox bounds) {
    return new MapConfig(styleUrl, tilesOverrideUrl, bounds);
  }
}
