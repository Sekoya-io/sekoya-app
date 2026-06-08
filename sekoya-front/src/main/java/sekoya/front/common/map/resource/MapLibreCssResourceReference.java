package sekoya.front.common.map.resource;

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;

public class MapLibreCssResourceReference extends WebjarsCssResourceReference {

  private static final long serialVersionUID = 1L;

  private static final MapLibreCssResourceReference INSTANCE = new MapLibreCssResourceReference();

  public MapLibreCssResourceReference() {
    super("maplibre-gl/current/dist/maplibre-gl.css");
  }

  public static MapLibreCssResourceReference get() {
    return INSTANCE;
  }
}
