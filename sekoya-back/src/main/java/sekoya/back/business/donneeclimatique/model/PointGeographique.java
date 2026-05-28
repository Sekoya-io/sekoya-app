package sekoya.back.business.donneeclimatique.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.bindgen.Bindable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.locationtech.jts.geom.Point;

@Entity
@Bindable
@Cacheable
public class PointGeographique extends GenericEntity<Long, PointGeographique> {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  @Column(unique = true)
  private Long idDrias;

  @JdbcTypeCode(SqlTypes.GEOGRAPHY)
  private Point localisation;

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
}
