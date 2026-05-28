package sekoya.back.business.donneeclimatique.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.bindgen.Bindable;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;

@Entity
@Bindable
@Cacheable
@Table(
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"pointgeographique_id", "aleatype", "scenario", "horizon"}))
public class DonneeClimatique extends GenericEntity<Long, DonneeClimatique> {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private DonneeClimatiqueBrute donneeClimatiqueBrute;

  @ManyToOne(optional = false)
  private PointGeographique pointGeographique;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private AleaType aleaType;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Scenario scenario;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Horizon horizon;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Evolution evolution;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public DonneeClimatiqueBrute getDonneeClimatiqueBrute() {
    return donneeClimatiqueBrute;
  }

  public void setDonneeClimatiqueBrute(DonneeClimatiqueBrute donneeClimatiqueBrute) {
    this.donneeClimatiqueBrute = donneeClimatiqueBrute;
  }

  public PointGeographique getPointGeographique() {
    return pointGeographique;
  }

  public void setPointGeographique(PointGeographique pointGeographique) {
    this.pointGeographique = pointGeographique;
  }

  public AleaType getAleaType() {
    return aleaType;
  }

  public void setAleaType(AleaType aleaType) {
    this.aleaType = aleaType;
  }

  public Horizon getHorizon() {
    return horizon;
  }

  public void setHorizon(Horizon horizon) {
    this.horizon = horizon;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  public Evolution getEvolution() {
    return evolution;
  }

  public void setEvolution(Evolution evolution) {
    this.evolution = evolution;
  }
}
