package sekoya.back.api.geocodage.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import sekoya.back.api.geocodage.bean.atomic.GeocodageAddressPropertiesType;

public class GeocodageAddressPropertiesTypeDeserializer
    extends JsonDeserializer<GeocodageAddressPropertiesType> {

  @Override
  public GeocodageAddressPropertiesType deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    String enumLabel = p.getText();
    try {
      return GeocodageAddressPropertiesType.getByLabel(enumLabel);
    } catch (Exception e) {
      throw new JsonParseException(p, "Impossible d'extraire l'enum " + enumLabel);
    }
  }
}
