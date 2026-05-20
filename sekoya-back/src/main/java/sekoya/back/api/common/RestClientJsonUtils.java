package sekoya.back.api.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.iglooproject.spring.util.StringUtils;

public final class RestClientJsonUtils {

  public static <T> T deserialize(String json, Class<T> beanClass) throws IOException {
    if (!StringUtils.hasText(json)) {
      throw new IOException("Erreur de désérialisation : aucun JSON renvoyé");
    }
    try {
      return JsonMapperUtils.DEFAULT_JSON_MAPPER.readerFor(beanClass).readValue(json);
    } catch (IOException e) {
      throw new IOException(
          String.format(
              "Erreur de désérialisation d'un bean %1$s:\n %2$s", beanClass.getSimpleName(), json),
          e);
    }
  }

  public static <T> List<T> deserializeAsList(String json, Class<T> beanClass) throws IOException {
    if (!StringUtils.hasText(json)) {
      throw new IOException("Erreur de désérialisation : aucun JSON renvoyé");
    }

    JavaType listJavaType =
        JsonMapperUtils.DEFAULT_JSON_MAPPER
            .getTypeFactory()
            .constructCollectionType(List.class, beanClass);
    try {
      return JsonMapperUtils.DEFAULT_JSON_MAPPER.readerFor(listJavaType).readValue(json);
    } catch (IOException e) {
      throw new IOException(
          String.format("Erreur de désérialisation d'un bean %1$s", beanClass.getSimpleName()), e);
    }
  }

  public static String serialize(Object serilizableBean) throws IOException {
    return serialize(serilizableBean, null);
  }

  public static String serialize(Object serilizableBean, Class<?> viewClass) throws IOException {
    Objects.requireNonNull(serilizableBean, "serilizableBean obligatoire");

    try {
      return JsonMapperUtils.DEFAULT_JSON_MAPPER.writeValueAsString(serilizableBean);
    } catch (JsonProcessingException e) {
      throw new IOException(
          String.format(
              "Erreur de sérialisation d'un bean %1$s", serilizableBean.getClass().getSimpleName()),
          e);
    }
  }

  private RestClientJsonUtils() {}
}
