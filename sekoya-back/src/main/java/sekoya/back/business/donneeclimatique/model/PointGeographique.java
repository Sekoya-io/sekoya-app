package sekoya.back.business.donneeclimatique.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.bindgen.Bindable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.locationtech.jts.geom.Point;
import sekoya.back.business.common.model.Latitude;
import sekoya.back.business.common.model.Longitude;
import sekoya.back.hibernate.type.LatitudeType;
import sekoya.back.hibernate.type.LongitudeType;

@Entity
@Bindable
@Cacheable
public class PointGeographique extends GenericEntity<Long, PointGeographique> {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  @Column(unique = true)
  private Long idDrias;

  @Basic(optional = false)
  @JdbcTypeCode(SqlTypes.GEOGRAPHY)
  private Point localisation;

  // DO NOT USE : uniquement pour Metabase
  @Basic(optional = false)
  @Type(LatitudeType.class)
  private Latitude latitude;

  // DO NOT USE : uniquement pour Metabase
  @Basic(optional = false)
  @Type(LongitudeType.class)
  private Longitude longitude;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Long getIdDrias() {
    return idDrias;
  }

  public void setIdDrias(Long idDrias) {
    this.idDrias = idDrias;
  }

  public Point getLocalisation() {
    return localisation;
  }

  public void setLocalisation(Point localisation) {
    this.localisation = localisation;
  }

  public Latitude getLatitude() {
    return latitude;
  }

  public void setLatitude(Latitude latitude) {
    this.latitude = latitude;
  }

  public Longitude getLongitude() {
    return longitude;
  }

  public void setLongitude(Longitude longitude) {
    this.longitude = longitude;
  }
}
