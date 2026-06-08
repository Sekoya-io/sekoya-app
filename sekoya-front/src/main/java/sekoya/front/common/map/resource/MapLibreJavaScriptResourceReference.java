package sekoya.front.common.map.resource;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import java.util.List;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;

public class MapLibreJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final MapLibreJavaScriptResourceReference INSTANCE =
      new MapLibreJavaScriptResourceReference();

  public MapLibreJavaScriptResourceReference() {
    super("maplibre-gl/current/dist/maplibre-gl.js");
  }

  public static MapLibreJavaScriptResourceReference get() {
    return INSTANCE;
  }

  @Override
  public List<HeaderItem> getDependencies() {
    List<HeaderItem> dependencies = super.getDependencies();
    dependencies.add(CssHeaderItem.forReference(MapLibreCssResourceReference.get()));
    return dependencies;
  }
}
