package sekoya.back.api.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RestClientServiceUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestClientServiceUtils.class);

  public static final Function<InputStream, String> IS_TO_STRING = new IsToStringFunction();

  public static String toFormUrlEncodedBody(Map<String, String> formFields) {
    return formFields.entrySet().stream()
        .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));
  }

  public static boolean checkSuccess(HttpResponse<String> httpResponse, String requestUri)
      throws RestClientCommunicationException {
    return checkSuccess(httpResponse, requestUri, s -> s);
  }

  public static <T> boolean checkSuccess(
      HttpResponse<T> httpResponse, String requestUri, Function<T, String> readResponseFunction)
      throws RestClientCommunicationException {
    int responseCode = httpResponse.statusCode();

    try {
      // 200 : OK
      if (responseCode > 199 && responseCode < 300) {
        return true;

        // 300 : Trop de redirections
      } else if (responseCode > 299 && responseCode < 400) {
        LOGGER.error(
            "Redirection %1$s (%2$s) : %3$s"
                .formatted(
                    responseCode, requestUri, readResponseFunction.apply(httpResponse.body())));
        throw new RestClientCommunicationException(
            "Erreur lors d'un appel REST : Code %1$d, trop de redirections"
                .formatted(responseCode));

        // 400 : Erreur client
      } else if (responseCode > 399 && responseCode < 500) {
        LOGGER.error(
            "Erreur %1$s (%2$s) : %3$s"
                .formatted(
                    responseCode, requestUri, readResponseFunction.apply(httpResponse.body())));
        throw new RestClientCommunicationException(
            "Erreur lors d'un appel REST : Code %1$d, erreur client".formatted(responseCode));

        // 500 : Erreur serveur
      } else {
        LOGGER.error(
            "Erreur %1$s (%2$s) : %3$s"
                .formatted(
                    responseCode, requestUri, readResponseFunction.apply(httpResponse.body())));
        throw new RestClientCommunicationException(
            "Erreur lors d'un appel REST : Code %1$d, erreur serveur".formatted(responseCode));
      }
    } catch (UnsupportedOperationException e) {
      throw new RestClientCommunicationException(
          "Une erreur inattendue s'est produite lors d'un appel REST", e);
    }
  }

  private static class IsToStringFunction implements Function<InputStream, String> {
    @Override
    public String apply(InputStream is) {
      try {
        return IOUtils.toString(is, StandardCharsets.UTF_8);
      } catch (IOException e) {
        LOGGER.error("Erreur lors de la lecture de la réponse d'un appel REST", e);
        return "[Erreur de lecture de la réponse]";
      }
    }
  }

  private RestClientServiceUtils() {}
}
