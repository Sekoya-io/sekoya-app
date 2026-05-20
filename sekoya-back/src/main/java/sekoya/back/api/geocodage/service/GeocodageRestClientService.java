package sekoya.back.api.geocodage.service;

import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_AUTOCOMPLETE;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_AUTOCOMPLETE_VALUE;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_GEOMETRY;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_GEOMETRY_VALUE;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_INDEX;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_INDEX_VALUE;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_LIMIT;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_LIMIT_VALUE;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PARAM_QUERY;
import static sekoya.back.api.geocodage.util.GeocodageRestClientConstant.PATH_SEARCH;
import static sekoya.back.property.SekoyaBackPropertyIds.API_COMMON_REQUEST_TIMEOUT;
import static sekoya.back.property.SekoyaBackPropertyIds.API_COMMON_RESPONSE_CONNECT_TIMEOUT;
import static sekoya.back.property.SekoyaBackPropertyIds.API_REST_CLIENT_GEOCODAGE_URL;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import sekoya.back.api.common.HttpHeaderBuilder;
import sekoya.back.api.common.RestClientCommunicationException;
import sekoya.back.api.common.RestClientJsonUtils;
import sekoya.back.api.common.RestClientServiceUtils;
import sekoya.back.api.geocodage.bean.GeocodageResponseBean;

@Service
public class GeocodageRestClientService implements IGeocodageRestClientService {

  private final IPropertyService propertyService;

  private final HttpClient httpClient;

  @Autowired
  public GeocodageRestClientService(IPropertyService propertyService) {
    this.propertyService = propertyService;
    this.httpClient =
        HttpClient.newBuilder()
            .version(Version.HTTP_1_1)
            .connectTimeout(
                Duration.ofSeconds(propertyService.get(API_COMMON_RESPONSE_CONNECT_TIMEOUT)))
            .build();
  }

  private HttpRequest.Builder getRequestBuilder(URI uri) throws URISyntaxException {
    return HttpRequest.newBuilder()
        .version(Version.HTTP_1_1)
        .timeout(Duration.ofSeconds(propertyService.get(API_COMMON_REQUEST_TIMEOUT)))
        .uri(uri)
        .headers(new HttpHeaderBuilder().contentTypeJson().build());
  }

  @Override
  public GeocodageResponseBean getSearch(String query) throws RestClientCommunicationException {
    Objects.requireNonNull(query);

    String baseUrl = propertyService.get(API_REST_CLIENT_GEOCODAGE_URL);
    UriComponentsBuilder uriBuilder =
        UriComponentsBuilder.fromUriString(
                baseUrl + (baseUrl.endsWith("/") ? "" : "/") + PATH_SEARCH)
            .queryParam(PARAM_QUERY, query)
            .queryParam(PARAM_AUTOCOMPLETE, PARAM_AUTOCOMPLETE_VALUE)
            .queryParam(PARAM_INDEX, PARAM_INDEX_VALUE)
            .queryParam(PARAM_LIMIT, PARAM_LIMIT_VALUE)
            .queryParam(PARAM_GEOMETRY, PARAM_GEOMETRY_VALUE);

    try {
      HttpRequest request = getRequestBuilder(uriBuilder.build().toUri()).GET().build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

      RestClientServiceUtils.checkSuccess(response, response.uri().toString());

      return RestClientJsonUtils.deserialize(response.body(), GeocodageResponseBean.class);
    } catch (URISyntaxException | IOException | InterruptedException e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }

      throw new RestClientCommunicationException(
          "Erreur lors de la communication avec géocodage", e);
    }
  }
}
