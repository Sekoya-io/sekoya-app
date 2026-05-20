package sekoya.back.api.common;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonMapperUtils {

  public static final ObjectMapper DEFAULT_JSON_MAPPER =
      new ObjectMapper()
          .setDefaultPropertyInclusion(Include.NON_EMPTY)
          .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
          .enable(SerializationFeature.INDENT_OUTPUT)
          .registerModule(new JavaTimeModule());

  private JsonMapperUtils() {}
}
