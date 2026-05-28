package sekoya.back.business.donneeclimatique.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import org.bindgen.Bindable;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Indicateur;
import sekoya.back.business.common.model.atomic.Saison;
import sekoya.back.business.common.model.atomic.Scenario;

@Entity
@Bindable
@Cacheable
@Table(
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"pointgeographique_id", "indicateur", "scenario", "horizon", "saison"}))
public class DonneeClimatiqueBrute extends GenericEntity<Long, DonneeClimatiqueBrute> {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @ManyToOne(optional = false)
  private PointGeographique pointGeographique;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Indicateur indicateur;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Scenario scenario;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Horizon horizon;

  @Basic
  @Enumerated(EnumType.STRING)
  private Saison saison;

  @Basic(optional = false)
  @Column(precision = 5, scale = 2)
  private BigDecimal ecart;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public PointGeographique getPointGeographique() {
    return pointGeographique;
  }

  public void setPointGeographique(PointGeographique pointGeographique) {
    this.pointGeographique = pointGeographique;
  }

  public Indicateur getIndicateur() {
    return indicateur;
  }

  public void setIndicateur(Indicateur indicateur) {
    this.indicateur = indicateur;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  public Horizon getHorizon() {
    return horizon;
  }

  public void setHorizon(Horizon horizon) {
    this.horizon = horizon;
  }

  public Saison getSaison() {
    return saison;
  }

  public void setSaison(Saison saison) {
    this.saison = saison;
  }

  public BigDecimal getEcart() {
    return ecart;
  }

  public void setEcart(BigDecimal ecart) {
    this.ecart = ecart;
  }
}
