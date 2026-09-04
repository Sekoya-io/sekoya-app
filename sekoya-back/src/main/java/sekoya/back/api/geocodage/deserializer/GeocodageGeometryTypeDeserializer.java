package sekoya.back.api.geocodage.deserializer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import sekoya.back.api.geocodage.bean.atomic.GeocodageGeometryType;

public class GeocodageGeometryTypeDeserializer extends ValueDeserializer<GeocodageGeometryType> {

  @Override
  public GeocodageGeometryType deserialize(JsonParser p, DeserializationContext ctxt)
      throws JacksonException {
    String enumLabel = p.getString();
    try {
      return GeocodageGeometryType.getByLabel(enumLabel);
    } catch (Exception e) {
      throw new StreamReadException(p, "Impossible d'extraire l'enum " + enumLabel);
    }
  }
}
