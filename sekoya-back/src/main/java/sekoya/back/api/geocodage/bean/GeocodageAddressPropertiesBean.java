package sekoya.back.api.geocodage.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import java.math.BigDecimal;
import sekoya.back.api.geocodage.bean.atomic.GeocodageAddressPropertiesType;
import sekoya.back.api.geocodage.deserializer.GeocodageAddressPropertiesTypeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodageAddressPropertiesBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String label;

  private String name;

  private String context;

  private BigDecimal score;

  private String depcode;

  private String postcode;

  private String citycode;

  private String city;

  private String district;

  private String street;

  private String housenumber;

  @JsonDeserialize(using = GeocodageAddressPropertiesTypeDeserializer.class)
  private GeocodageAddressPropertiesType type;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  public String getDepcode() {
    return depcode;
  }

  public void setDepcode(String depcode) {
    this.depcode = depcode;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getCitycode() {
    return citycode;
  }

  public void setCitycode(String citycode) {
    this.citycode = citycode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getHousenumber() {
    return housenumber;
  }

  public void setHousenumber(String housenumber) {
    this.housenumber = housenumber;
  }

  public GeocodageAddressPropertiesType getType() {
    return type;
  }

  public void setType(GeocodageAddressPropertiesType type) {
    this.type = type;
  }
}
