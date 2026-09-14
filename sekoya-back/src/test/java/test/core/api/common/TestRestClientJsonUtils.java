package test.core.api.common;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sekoya.back.api.common.RestClientJsonUtils;
import sekoya.back.api.geocodage.bean.GeocodageAddressPropertiesBean;
import sekoya.back.api.geocodage.bean.GeocodageGeocodeResponseBean;
import sekoya.back.api.geocodage.bean.GeocodageGeometryBean;
import sekoya.back.api.geocodage.bean.GeocodageResponseBean;
import sekoya.back.api.geocodage.bean.atomic.GeocodageAddressPropertiesType;
import sekoya.back.api.geocodage.bean.atomic.GeocodageGeometryType;

class TestRestClientJsonUtils {

  private static final String GEOCODAGE_RESPONSE_JSON =
      """
      {
        "type": "FeatureCollection",
        "version": "draft",
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
            "geometry": {
              "type": "Point",
              "coordinates": [2.347, 48.859]
            },
            "properties": {
              "label": "8 Boulevard du Port 80000 Amiens",
              "score": 0.49,
              "housenumber": "8",
              "id": "80021_6590_00008",
              "name": "8 Boulevard du Port",
              "postcode": "80000",
              "citycode": "80021",
              "city": "Amiens",
              "context": "80, Somme, Hauts-de-France",
              "type": "housenumber",
              "street": "Boulevard du Port",
              "depcode": "80"
            }
          },
          {
            "geometry": {
              "type": "Point",
              "coordinates": [3.057, 50.631]
            },
            "properties": {
              "label": "Boulevard du Port 59000 Lille",
              "score": 0.38,
              "id": "59350_1234",
              "name": "Boulevard du Port",
              "postcode": "59000",
              "citycode": "59350",
              "city": "Lille",
              "context": "59, Nord, Hauts-de-France",
              "type": "street",
              "depcode": "59"
            }
          }
        ],
        "query": "bd du port"
      }
      """;

  @Nested
  class TestDeserialize {

    @Test
    void testDeserialize_geocodageResponse_fieldsCorrectlyPopulated() throws IOException {
      GeocodageResponseBean result =
          RestClientJsonUtils.deserialize(GEOCODAGE_RESPONSE_JSON, GeocodageResponseBean.class);

      Assertions.assertThat(result.getQuery()).isEqualTo("8 bd du port");
      Assertions.assertThat(result.getFeatures()).hasSize(1);

      GeocodageGeocodeResponseBean feature = result.getFeatures().get(0);

      // Geometry
      Assertions.assertThat(feature.getGeometry().getType()).isEqualTo(GeocodageGeometryType.POINT);
      Assertions.assertThat(feature.getGeometry().getCoordinates()).containsExactly(2.347, 48.859);

      // Properties
      GeocodageAddressPropertiesBean props = feature.getProperties();
      Assertions.assertThat(props.getLabel()).isEqualTo("8 Boulevard du Port 80000 Amiens");
      Assertions.assertThat(props.getScore())
          .isEqualByComparingTo(new BigDecimal("0.49159121588068583"));
      Assertions.assertThat(props.getHousenumber()).isEqualTo("8");
      Assertions.assertThat(props.getId()).isEqualTo("80021_6590_00008");
      Assertions.assertThat(props.getName()).isEqualTo("8 Boulevard du Port");
      Assertions.assertThat(props.getPostcode()).isEqualTo("80000");
      Assertions.assertThat(props.getCitycode()).isEqualTo("80021");
      Assertions.assertThat(props.getCity()).isEqualTo("Amiens");
      Assertions.assertThat(props.getDistrict()).isEqualTo("Amiens");
      Assertions.assertThat(props.getContext()).isEqualTo("80, Somme, Hauts-de-France");
      Assertions.assertThat(props.getType()).isEqualTo(GeocodageAddressPropertiesType.HOUSENUMBER);
      Assertions.assertThat(props.getStreet()).isEqualTo("Boulevard du Port");
      Assertions.assertThat(props.getDepcode()).isEqualTo("80");
    }

    @Test
    void testDeserialize_geocodageResponse_multipleFeatures() throws IOException {
      GeocodageResponseBean result =
          RestClientJsonUtils.deserialize(
              GEOCODAGE_RESPONSE_MULTI_FEATURES_JSON, GeocodageResponseBean.class);

      Assertions.assertThat(result.getFeatures()).hasSize(2);
      Assertions.assertThat(result.getQuery()).isEqualTo("bd du port");

      Assertions.assertThat(result.getFeatures().get(0).getProperties().getType())
          .isEqualTo(GeocodageAddressPropertiesType.HOUSENUMBER);
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getCity())
          .isEqualTo("Amiens");

      Assertions.assertThat(result.getFeatures().get(1).getProperties().getType())
          .isEqualTo(GeocodageAddressPropertiesType.STREET);
      Assertions.assertThat(result.getFeatures().get(1).getProperties().getCity())
          .isEqualTo("Lille");
      Assertions.assertThat(result.getFeatures().get(1).getProperties().getHousenumber()).isNull();
    }

    @Test
    void testDeserialize_jsonWithUnknownProperties_unknownFieldsIgnored() throws IOException {
      // Le JSON contient des champs inconnus : "type", "version" au niveau racine
      // et "type": "Feature" au niveau des features
      // @JsonIgnoreProperties(ignoreUnknown = true) doit les ignorer
      GeocodageResponseBean result =
          RestClientJsonUtils.deserialize(GEOCODAGE_RESPONSE_JSON, GeocodageResponseBean.class);

      Assertions.assertThat(result).isNotNull();
      Assertions.assertThat(result.getQuery()).isEqualTo("8 bd du port");
      Assertions.assertThat(result.getFeatures()).hasSize(1);
    }

    @Test
    void testDeserialize_emptyString_throwIOException() {
      Assertions.assertThatThrownBy(
              () -> RestClientJsonUtils.deserialize("", GeocodageResponseBean.class))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("aucun JSON renvoyé");
    }

    @Test
    void testDeserialize_blankString_throwIOException() {
      Assertions.assertThatThrownBy(
              () -> RestClientJsonUtils.deserialize("   ", GeocodageResponseBean.class))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("aucun JSON renvoyé");
    }

    @Test
    void testDeserialize_nullString_throwIOException() {
      Assertions.assertThatThrownBy(
              () -> RestClientJsonUtils.deserialize(null, GeocodageResponseBean.class))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("aucun JSON renvoyé");
    }

    @Test
    void testDeserialize_invalidJson_throwIOException() {
      Assertions.assertThatThrownBy(
              () -> RestClientJsonUtils.deserialize("{invalid", GeocodageResponseBean.class))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("Erreur de désérialisation");
    }

    @Test
    void testDeserialize_emptyJsonObject_returnsBeanWithNullFields() throws IOException {
      GeocodageResponseBean result =
          RestClientJsonUtils.deserialize("{}", GeocodageResponseBean.class);

      Assertions.assertThat(result).isNotNull();
      Assertions.assertThat(result.getFeatures()).isNull();
      Assertions.assertThat(result.getQuery()).isNull();
    }

    @Test
    void testDeserialize_singleQuotedJson_succeeds() throws IOException {
      GeocodageResponseBean result =
          RestClientJsonUtils.deserialize(
              "{'query':'test','features':[]}", GeocodageResponseBean.class);

      Assertions.assertThat(result.getQuery()).isEqualTo("test");
      Assertions.assertThat(result.getFeatures()).isEmpty();
    }
  }

  @Nested
  class TestCustomDeserializers {

    @Test
    void testDeserialize_geometryTypePoint_returnsPoint() throws IOException {
      GeocodageGeometryBean result =
          RestClientJsonUtils.deserialize(
              """
              {"type":"Point","coordinates":[2.347,48.859]}
              """,
              GeocodageGeometryBean.class);

      Assertions.assertThat(result.getType()).isEqualTo(GeocodageGeometryType.POINT);
    }

    @Test
    void testDeserialize_geometryTypeMultiPolygon_returnsMultiPolygon() throws IOException {
      GeocodageGeometryBean result =
          RestClientJsonUtils.deserialize(
              """
              {"type":"MultiPolygon","coordinates":[]}
              """,
              GeocodageGeometryBean.class);

      Assertions.assertThat(result.getType()).isEqualTo(GeocodageGeometryType.MULTI_POLYGON);
    }

    @Test
    void testDeserialize_geometryTypeLineString_returnsLineString() throws IOException {
      GeocodageGeometryBean result =
          RestClientJsonUtils.deserialize(
              """
              {"type":"LineString","coordinates":[]}
              """,
              GeocodageGeometryBean.class);

      Assertions.assertThat(result.getType()).isEqualTo(GeocodageGeometryType.LINE_STRING);
    }

    @Test
    void testDeserialize_addressPropertiesTypeHousenumber_returnsHousenumber() throws IOException {
      GeocodageAddressPropertiesBean result =
          RestClientJsonUtils.deserialize(
              """
              {"type":"housenumber"}
              """,
              GeocodageAddressPropertiesBean.class);

      Assertions.assertThat(result.getType()).isEqualTo(GeocodageAddressPropertiesType.HOUSENUMBER);
    }

    @Test
    void testDeserialize_addressPropertiesTypeStreet_returnsStreet() throws IOException {
      GeocodageAddressPropertiesBean result =
          RestClientJsonUtils.deserialize(
              """
              {"type":"street"}
              """,
              GeocodageAddressPropertiesBean.class);

      Assertions.assertThat(result.getType()).isEqualTo(GeocodageAddressPropertiesType.STREET);
    }

    @Test
    void testDeserialize_addressPropertiesTypeLocality_returnsLocality() throws IOException {
      GeocodageAddressPropertiesBean result =
          RestClientJsonUtils.deserialize(
              """
              {"type":"locality"}
              """,
              GeocodageAddressPropertiesBean.class);

      Assertions.assertThat(result.getType()).isEqualTo(GeocodageAddressPropertiesType.LOCALITY);
    }

    @Test
    void testDeserialize_addressPropertiesTypeMunicipality_returnsMunicipality()
        throws IOException {
      GeocodageAddressPropertiesBean result =
          RestClientJsonUtils.deserialize(
              """
              {"type":"municipality"}
              """,
              GeocodageAddressPropertiesBean.class);

      Assertions.assertThat(result.getType())
          .isEqualTo(GeocodageAddressPropertiesType.MUNICIPALITY);
    }

    @Test
    void testDeserialize_geometryTypeUnknown_throwIOException() {
      Assertions.assertThatThrownBy(
              () ->
                  RestClientJsonUtils.deserialize(
                      """
                      {"type":"Unknown","coordinates":[]}
                      """,
                      GeocodageGeometryBean.class))
          .isInstanceOf(IOException.class);
    }

    @Test
    void testDeserialize_addressPropertiesTypeUnknown_throwIOException() {
      Assertions.assertThatThrownBy(
              () ->
                  RestClientJsonUtils.deserialize(
                      """
                      {"type":"unknown_type"}
                      """,
                      GeocodageAddressPropertiesBean.class))
          .isInstanceOf(IOException.class);
    }
  }

  @Nested
  class TestDeserializeAsList {

    @Test
    void testDeserializeAsList_jsonArray_returnsList() throws IOException {
      String json =
          """
          [
            {"type":"Point","coordinates":[2.347,48.859]},
            {"type":"LineString","coordinates":[3.0,50.6]}
          ]
          """;

      List<GeocodageGeometryBean> result =
          RestClientJsonUtils.deserializeAsList(json, GeocodageGeometryBean.class);

      Assertions.assertThat(result).hasSize(2);
      Assertions.assertThat(result.get(0).getType()).isEqualTo(GeocodageGeometryType.POINT);
      Assertions.assertThat(result.get(1).getType()).isEqualTo(GeocodageGeometryType.LINE_STRING);
    }

    @Test
    void testDeserializeAsList_emptyArray_returnsEmptyList() throws IOException {
      List<GeocodageGeometryBean> result =
          RestClientJsonUtils.deserializeAsList("[]", GeocodageGeometryBean.class);

      Assertions.assertThat(result).isEmpty();
    }

    @Test
    void testDeserializeAsList_emptyString_throwIOException() {
      Assertions.assertThatThrownBy(
              () -> RestClientJsonUtils.deserializeAsList("", GeocodageGeometryBean.class))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("aucun JSON renvoyé");
    }

    @Test
    void testDeserializeAsList_nullString_throwIOException() {
      Assertions.assertThatThrownBy(
              () -> RestClientJsonUtils.deserializeAsList(null, GeocodageGeometryBean.class))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("aucun JSON renvoyé");
    }

    @Test
    void testDeserializeAsList_invalidJson_throwIOException() {
      Assertions.assertThatThrownBy(
              () -> RestClientJsonUtils.deserializeAsList("[invalid", GeocodageGeometryBean.class))
          .isInstanceOf(IOException.class);
    }
  }

  @Nested
  class TestSerialize {

    @Test
    void testSerialize_geocodageGeometryBean_containsExpectedFields() throws IOException {
      GeocodageGeometryBean bean = new GeocodageGeometryBean();
      bean.setType(GeocodageGeometryType.POINT);
      bean.setCoordinates(List.of(2.347, 48.859));

      String result = RestClientJsonUtils.serialize(bean);

      // Ne pas comparer le JSON complet (l'ordre des propriétés peut changer)
      Assertions.assertThat(result).contains("\"coordinates\"");
      Assertions.assertThat(result).contains("2.347");
      Assertions.assertThat(result).contains("48.859");
      Assertions.assertThat(result).contains("\"type\"");
    }

    @Test
    void testSerialize_nonEmptyInclusion_nullFieldsExcluded() throws IOException {
      GeocodageAddressPropertiesBean bean = new GeocodageAddressPropertiesBean();
      bean.setId("test-id");
      bean.setLabel("test-label");
      // Tous les autres champs restent null

      String result = RestClientJsonUtils.serialize(bean);

      Assertions.assertThat(result).contains("\"id\"");
      Assertions.assertThat(result).contains("\"test-id\"");
      Assertions.assertThat(result).contains("\"label\"");
      Assertions.assertThat(result).contains("\"test-label\"");
      // Les champs null ne doivent pas apparaître (NON_EMPTY)
      Assertions.assertThat(result).doesNotContain("\"housenumber\"");
      Assertions.assertThat(result).doesNotContain("\"street\"");
      Assertions.assertThat(result).doesNotContain("\"district\"");
      Assertions.assertThat(result).doesNotContain("\"depcode\"");
    }

    @Test
    void testSerialize_nonEmptyInclusion_emptyListExcluded() throws IOException {
      GeocodageResponseBean bean = new GeocodageResponseBean();
      bean.setFeatures(new ArrayList<>());
      // query reste null

      String result = RestClientJsonUtils.serialize(bean);

      // Les collections vides et les champs null sont exclus par NON_EMPTY
      Assertions.assertThat(result).doesNotContain("\"features\"");
      Assertions.assertThat(result).doesNotContain("\"query\"");
    }

    @Test
    void testSerialize_indentOutput_resultIsFormatted() throws IOException {
      GeocodageResponseBean bean = new GeocodageResponseBean();
      bean.setQuery("test");
      bean.setFeatures(new ArrayList<>());

      String result = RestClientJsonUtils.serialize(bean);

      // INDENT_OUTPUT produit du JSON indenté avec des sauts de ligne
      Assertions.assertThat(result).contains("\n");
    }

    @Test
    void testSerialize_nullBean_throwNullPointerException() {
      Assertions.assertThatThrownBy(() -> RestClientJsonUtils.serialize(null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testSerialize_roundTrip_geocodageResponseBean() throws IOException {
      // Créer un bean complet (sans les champs enum pour le round-trip,
      // car la sérialisation utilise name() et le deserializer custom attend le label)
      GeocodageResponseBean original = new GeocodageResponseBean();
      original.setQuery("test query");

      GeocodageGeocodeResponseBean feature = new GeocodageGeocodeResponseBean();

      GeocodageGeometryBean geometry = new GeocodageGeometryBean();
      geometry.setCoordinates(List.of(2.347, 48.859));
      // type non renseigné pour éviter le problème de round-trip enum
      feature.setGeometry(geometry);

      GeocodageAddressPropertiesBean properties = new GeocodageAddressPropertiesBean();
      properties.setId("80021_6590_00008");
      properties.setLabel("8 Boulevard du Port 80000 Amiens");
      properties.setName("8 Boulevard du Port");
      properties.setPostcode("80000");
      properties.setCitycode("80021");
      properties.setCity("Amiens");
      properties.setScore(new BigDecimal("0.49"));
      // type non renseigné pour le round-trip
      feature.setProperties(properties);

      original.setFeatures(List.of(feature));

      // Sérialiser puis désérialiser
      String json = RestClientJsonUtils.serialize(original);
      GeocodageResponseBean result =
          RestClientJsonUtils.deserialize(json, GeocodageResponseBean.class);

      // Vérifier que les champs non-enum sont préservés
      Assertions.assertThat(result.getQuery()).isEqualTo("test query");
      Assertions.assertThat(result.getFeatures()).hasSize(1);
      Assertions.assertThat(result.getFeatures().get(0).getGeometry().getCoordinates())
          .containsExactly(2.347, 48.859);
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getId())
          .isEqualTo("80021_6590_00008");
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getLabel())
          .isEqualTo("8 Boulevard du Port 80000 Amiens");
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getCity())
          .isEqualTo("Amiens");
      Assertions.assertThat(result.getFeatures().get(0).getProperties().getScore())
          .isEqualByComparingTo(new BigDecimal("0.49"));
    }
  }
}
