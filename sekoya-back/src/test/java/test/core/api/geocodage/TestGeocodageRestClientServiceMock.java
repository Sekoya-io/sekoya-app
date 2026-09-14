package test.core.api.geocodage;

import static sekoya.back.property.SekoyaBackPropertyIds.API_COMMON_REQUEST_TIMEOUT;
import static sekoya.back.property.SekoyaBackPropertyIds.API_COMMON_RESPONSE_CONNECT_TIMEOUT;
import static sekoya.back.property.SekoyaBackPropertyIds.API_REST_CLIENT_GEOCODAGE_URL;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.assertj.core.api.Assertions;
import org.iglooproject.spring.property.service.IPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import sekoya.back.api.common.RestClientCommunicationException;
import sekoya.back.api.geocodage.bean.GeocodageResponseBean;
import sekoya.back.api.geocodage.bean.atomic.GeocodageAddressPropertiesType;
import sekoya.back.api.geocodage.bean.atomic.GeocodageGeometryType;
import sekoya.back.api.geocodage.service.GeocodageRestClientService;

@ExtendWith(MockitoExtension.class)
class TestGeocodageRestClientServiceMock {

  private static final String BASE_URL = "https://api-adresse.data.gouv.fr";

  private static final String GEOCODAGE_RESPONSE_JSON =
      """
      {
        "type": "FeatureCollection",
        "features": [
          {
            "type": "Feature",
            "geometry": {
              "type": "Point",
              "coordinates": [2.347, 48.859]
            },
            "properties": {
              "label": "8 Boulevard du Port 80000 Amiens",
              "score": 0.49159121588068583,
              "housenumber": "8",
              "id": "80021_6590_00008",
              "name": "8 Boulevard du Port",
              "postcode": "80000",
              "citycode": "80021",
              "city": "Amiens",
              "district": "Amiens",
              "context": "80, Somme, Hauts-de-France",
              "type": "housenumber",
              "street": "Boulevard du Port",
              "depcode": "80"
            }
          }
        ],
        "query": "8 bd du port"
      }
      """;

  private static final String GEOCODAGE_RESPONSE_MULTI_FEATURES_JSON =
      """
      {
        "features": [
          {
            "geometry": { "type": "Point", "coordinates": [2.347, 48.859] },
            "properties": {
              "label": "8 Boulevard du Port 80000 Amiens",
              "score": 0.49,
              "id": "80021_6590_00008",
              "type": "housenumber",
              "city": "Amiens",
              "depcode": "80"
            }
          },
          {
            "geometry": { "type": "Point", "coordinates": [3.057, 50.631] },
            "properties": {
              "label": "Boulevard du Port 59000 Lille",
              "score": 0.38,
              "id": "59350_1234",
              "type": "street",
              "city": "Lille",
              "depcode": "59"
            }
          }
        ],
        "query": "bd du port"
      }
      """;

  private static final String GEOCODAGE_RESPONSE_ALL_TYPES_JSON =
      """
      {
        "features": [
          {
            "geometry": { "type": "Point", "coordinates": [2.0, 48.0] },
            "properties": { "id": "1", "type": "housenumber", "label": "addr1" }
          },
          {
            "geometry": { "type": "Point", "coordinates": [3.0, 49.0] },
            "properties": { "id": "2", "type": "street", "label": "addr2" }
          },
          {
            "geometry": { "type": "Point", "coordinates": [4.0, 50.0] },
            "properties": { "id": "3", "type": "locality", "label": "addr3" }
          },
          {
            "geometry": { "type": "Point", "coordinates": [5.0, 51.0] },
            "properties": { "id": "4", "type": "municipality", "label": "addr4" }
          }
        ],
        "query": "test"
      }
      """;

  @Mock private IPropertyService propertyService;

  @Mock private HttpClient httpClient;

  @Mock private HttpResponse<String> httpResponse;

  private GeocodageRestClientService geocodageRestClientService;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setUp() throws IOException, InterruptedException {
    Mockito.lenient()
        .when(propertyService.get(API_COMMON_RESPONSE_CONNECT_TIMEOUT))
        .thenReturn(30L);
    Mockito.lenient().when(propertyService.get(API_COMMON_REQUEST_TIMEOUT)).thenReturn(30L);
    Mockito.lenient().when(propertyService.get(API_REST_CLIENT_GEOCODAGE_URL)).thenReturn(BASE_URL);

    geocodageRestClientService = new GeocodageRestClientService(propertyService);
    ReflectionTestUtils.setField(geocodageRestClientService, "httpClient", httpClient);
  }

  @SuppressWarnings("unchecked")
  private void mockHttpResponse(int statusCode, String body)
      throws IOException, InterruptedException {
    Mockito.when(httpResponse.statusCode()).thenReturn(statusCode);
    Mockito.when(httpResponse.body()).thenReturn(body);
    Mockito.when(httpResponse.uri()).thenReturn(URI.create(BASE_URL + "/search?q=test"));
    Mockito.when(
            httpClient.send(
                Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
  }

  @Nested
  class TestGetSearch {

    @Test
    void testGetSearch_validResponse_returnsGeocodageResponseBean() throws Exception {
      mockHttpResponse(200, GEOCODAGE_RESPONSE_JSON);

      GeocodageResponseBean result = geocodageRestClientService.getSearch("8 bd du port");

      Assertions.assertThat(result).isNotNull();
      Assertions.assertThat(result.getQuery()).isEqualTo("8 bd du port");
      Assertions.assertThat(result.getFeatures()).hasSize(1);

      Assertions.assertThat(result.getFeatures().get(0).getGeometry().getType())
          .isEqualTo(GeocodageGeometryType.POINT);
      Assertions.assertThat(result.getFeatures().get(0).getGeometry().getCoordinates())
          .containsExactly(2.347, 48.859);

      Assertions.assertThat(result.getFeatures().get(0).getProperties().getLabel())
          .isEqualTo("8 Boulevard du Port 80000 Amiens");
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getType())
          .isEqualTo(GeocodageAddressPropertiesType.HOUSENUMBER);
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getCity())
          .isEqualTo("Amiens");
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getPostcode())
          .isEqualTo("80000");
    }

    @Test
    void testGetSearch_multipleFeatures_allFeaturesDeserialized() throws Exception {
      mockHttpResponse(200, GEOCODAGE_RESPONSE_MULTI_FEATURES_JSON);

      GeocodageResponseBean result = geocodageRestClientService.getSearch("bd du port");

      Assertions.assertThat(result.getFeatures()).hasSize(2);
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getCity())
          .isEqualTo("Amiens");
      Assertions.assertThat(result.getFeatures().get(1).getProperties().getCity())
          .isEqualTo("Lille");
    }

    @Test
    void testGetSearch_emptyFeaturesArray_returnsEmptyList() throws Exception {
      mockHttpResponse(
          200,
          """
          {"features":[],"query":"adresse inexistante"}
          """);

      GeocodageResponseBean result = geocodageRestClientService.getSearch("adresse inexistante");

      Assertions.assertThat(result.getFeatures()).isEmpty();
      Assertions.assertThat(result.getQuery()).isEqualTo("adresse inexistante");
    }

    @Test
    void testGetSearch_allAddressPropertiesTypes_correctEnumMapping() throws Exception {
      mockHttpResponse(200, GEOCODAGE_RESPONSE_ALL_TYPES_JSON);

      GeocodageResponseBean result = geocodageRestClientService.getSearch("test");

      Assertions.assertThat(result.getFeatures()).hasSize(4);
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getType())
          .isEqualTo(GeocodageAddressPropertiesType.HOUSENUMBER);
      Assertions.assertThat(result.getFeatures().get(1).getProperties().getType())
          .isEqualTo(GeocodageAddressPropertiesType.STREET);
      Assertions.assertThat(result.getFeatures().get(2).getProperties().getType())
          .isEqualTo(GeocodageAddressPropertiesType.LOCALITY);
      Assertions.assertThat(result.getFeatures().get(3).getProperties().getType())
          .isEqualTo(GeocodageAddressPropertiesType.MUNICIPALITY);
    }
  }

  @Nested
  class TestGetSearchErrors {

    @Test
    void testGetSearch_nullQuery_throwNullPointerException() {
      Assertions.assertThatThrownBy(() -> geocodageRestClientService.getSearch(null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testGetSearch_httpError400_throwRestClientCommunicationException() throws Exception {
      mockHttpResponse(400, "Bad Request");

      Assertions.assertThatThrownBy(() -> geocodageRestClientService.getSearch("test"))
          .isInstanceOf(RestClientCommunicationException.class);
    }

    @Test
    void testGetSearch_httpError500_throwRestClientCommunicationException() throws Exception {
      mockHttpResponse(500, "Internal Server Error");

      Assertions.assertThatThrownBy(() -> geocodageRestClientService.getSearch("test"))
          .isInstanceOf(RestClientCommunicationException.class);
    }

    @Test
    void testGetSearch_httpError300_throwRestClientCommunicationException() throws Exception {
      mockHttpResponse(301, "Moved Permanently");

      Assertions.assertThatThrownBy(() -> geocodageRestClientService.getSearch("test"))
          .isInstanceOf(RestClientCommunicationException.class);
    }

    @Test
    void testGetSearch_invalidJsonResponse_throwRestClientCommunicationException()
        throws Exception {
      mockHttpResponse(200, "{invalid json");

      Assertions.assertThatThrownBy(() -> geocodageRestClientService.getSearch("test"))
          .isInstanceOf(RestClientCommunicationException.class);
    }

    @Test
    void testGetSearch_emptyBodyResponse_throwRestClientCommunicationException() throws Exception {
      mockHttpResponse(200, "");

      Assertions.assertThatThrownBy(() -> geocodageRestClientService.getSearch("test"))
          .isInstanceOf(RestClientCommunicationException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetSearch_ioException_throwRestClientCommunicationException() throws Exception {
      Mockito.when(
              httpClient.send(
                  Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
          .thenThrow(new IOException("Connection refused"));

      Assertions.assertThatThrownBy(() -> geocodageRestClientService.getSearch("test"))
          .isInstanceOf(RestClientCommunicationException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    void
        testGetSearch_interruptedException_throwRestClientCommunicationExceptionAndInterruptThread()
            throws Exception {
      Mockito.when(
              httpClient.send(
                  Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
          .thenThrow(new InterruptedException("Interrupted"));

      Assertions.assertThatThrownBy(() -> geocodageRestClientService.getSearch("test"))
          .isInstanceOf(RestClientCommunicationException.class);

      // Vérifier que le thread a bien été ré-interrompu
      Assertions.assertThat(Thread.currentThread().isInterrupted()).isTrue();

      // Nettoyer le flag d'interruption pour ne pas impacter les autres tests
      Thread.interrupted();
    }
  }
}
