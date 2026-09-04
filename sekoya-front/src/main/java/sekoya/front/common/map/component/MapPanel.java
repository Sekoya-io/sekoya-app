package sekoya.front.common.map.component;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;
import java.io.Serializable;
import java.util.Collection;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import sekoya.front.common.map.model.MapConfig;
import sekoya.front.common.map.model.MapPoint;
import sekoya.front.common.map.resource.MapLibreJavaScriptResourceReference;

public class MapPanel extends Panel {

  private static final long serialVersionUID = 1L;

  private static final JsonMapper JSON = buildJsonMapper();

  private static JsonMapper buildJsonMapper() {
    return JsonMapper.builder().build();
  }

  private static final JavaScriptResourceReference PANEL_JS =
      new JavaScriptResourceReference(MapPanel.class, "MapPanel.js");

  private final IModel<MapConfig> configModel;
  private final IModel<? extends Collection<MapPoint>> pointsModel;

  private final WebMarkupContainer mapContainer;

  private final AbstractDefaultAjaxBehavior clickBehavior;

  public MapPanel(String id, IModel<? extends Collection<MapPoint>> pointsModel) {
    this(id, pointsModel, Model.of(MapConfig.defaults()));
  }

  public MapPanel(
      String id,
      IModel<? extends Collection<MapPoint>> pointsModel,
      IModel<MapConfig> configModel) {
    super(id);
    this.pointsModel = pointsModel;
    this.configModel = configModel;

    this.mapContainer = new WebMarkupContainer("map");
    this.mapContainer.setOutputMarkupId(true);

    setOutputMarkupId(true);

    add(
        this.mapContainer.add(
            new ClassAttributeAppender(
                Condition.collectionModelNotEmpty(pointsModel)
                    .then(Model.of())
                    .otherwise(Model.of("map-blur")))));

    this.clickBehavior =
        new AbstractDefaultAjaxBehavior() {
          @Override
          protected void respond(AjaxRequestTarget target) {
            Long pointId =
                getRequest().getRequestParameters().getParameterValue("pointId").toLong();
            onPointClick(target, pointId);
          }
        };
    add(this.clickBehavior);
  }

  protected void onPointClick(AjaxRequestTarget target, Long pointId) {
    // no-op
  }

  public void updatePoints(AjaxRequestTarget target) {
    target.appendJavaScript(
        "MapPanel.updatePoints(%s, %s, %s);"
            .formatted(
                serialize(mapContainer.getMarkupId()),
                serialize(pointsModel.getObject()),
                serialize(clickBehavior.getCallbackUrl().toString())));
  }

  public void centerOnPoint(AjaxRequestTarget target, Long pointId) {
    target.appendJavaScript(
        "MapPanel.centerOnPoint(%s, %s);"
            .formatted(serialize(mapContainer.getMarkupId()), serialize(pointId)));
  }

  @Override
  public void renderHead(IHeaderResponse response) {
    super.renderHead(response);

    response.render(JavaScriptHeaderItem.forReference(MapLibreJavaScriptResourceReference.get()));
    response.render(JavaScriptHeaderItem.forReference(PANEL_JS));

    MapConfig config = configModel.getObject();
    MapConfig.BoundingBox fallback = config.fallbackBounds();
    MapInitConfig initConfig =
        new MapInitConfig(
            mapContainer.getMarkupId(),
            config.styleUrl(),
            config.tilesOverrideUrl(),
            fallback.swLng(),
            fallback.swLat(),
            fallback.neLng(),
            fallback.neLat(),
            pointsModel.getObject(),
            clickBehavior.getCallbackUrl().toString());

    var script = "MapPanel.init(%s);".formatted(serialize(initConfig));
    response.render(OnDomReadyHeaderItem.forScript(script));
  }

  private static String serialize(Object value) {
    try {
      return JSON.writeValueAsString(value);
    } catch (JacksonException e) {
      throw new WicketRuntimeException("Map panel JSON serialization failed", e);
    }
  }

  private record MapInitConfig(
      String containerId,
      String styleUrl,
      String tilesOverrideUrl,
      double fallbackSwLng,
      double fallbackSwLat,
      double fallbackNeLng,
      double fallbackNeLat,
      Collection<MapPoint> points,
      String callbackUrl)
      implements Serializable {}

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(configModel, pointsModel);
  }
}
