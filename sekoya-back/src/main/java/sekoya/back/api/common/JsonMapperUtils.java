package sekoya.back.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

public final class JsonMapperUtils {

  public static final ObjectMapper DEFAULT_JSON_MAPPER =
      JsonMapper.builder()
          .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_EMPTY))
          .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
          .enable(SerializationFeature.INDENT_OUTPUT)
          .build();

  private JsonMapperUtils() {}
}
