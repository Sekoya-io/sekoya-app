package sekoya.back.business.alea.model;

import com.google.common.base.MoreObjects.ToStringHelper;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Embedded;
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
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import sekoya.back.business.alea.model.atomic.AleaSensibilite;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.ImpactPotentiel;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.hibernate.search.bridge.EnumOrdinalValueBridge;

@Entity
@Bindable
@Cacheable
@Indexed
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"processus_id", "type"}))
public class Alea extends GenericEntity<Long, Alea> {

  private static final long serialVersionUID = 1L;

  public static final String PROCESSUS = "processus";
  public static final String TYPE = "type";
  public static final String SENSIBILITE = "sensibilite";
  public static final String IMPACT_POTENTIEL_BRUT = "impactPotentielBrut";

  @Id @GeneratedValue private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @GenericField(name = PROCESSUS, valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  private Processus processus;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @GenericField(
      name = TYPE,
      valueBridge = @ValueBridgeRef(type = EnumOrdinalValueBridge.class),
      sortable = Sortable.YES)
  private AleaType type;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @GenericField(name = SENSIBILITE)
  private AleaSensibilite sensibilite;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  @GenericField(
      name = IMPACT_POTENTIEL_BRUT,
      valueBridge = @ValueBridgeRef(type = EnumOrdinalValueBridge.class),
      sortable = Sortable.YES)
  private ImpactPotentiel impactPotentielBrut;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Evolution evolutionRcp45Annee2035;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Evolution evolutionRcp45Annee2055;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Evolution evolutionRcp85Annee2035;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Evolution evolutionRcp85Annee2055;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Risque risqueBrutRcp45Annee2035;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Risque risqueBrutRcp45Annee2055;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Risque risqueBrutRcp85Annee2035;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private Risque risqueBrutRcp85Annee2055;

  @Embedded private HistoryEventSummary creation;

  @Embedded private HistoryEventSummary modification;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Processus getProcessus() {
    return processus;
  }

  public void setProcessus(Processus processus) {
    this.processus = processus;
  }

  public AleaType getType() {
    return type;
  }

  public void setType(AleaType type) {
    this.type = type;
  }

  public AleaSensibilite getSensibilite() {
    return sensibilite;
  }

  public void setSensibilite(AleaSensibilite sensibilite) {
    this.sensibilite = sensibilite;
  }

  public ImpactPotentiel getImpactPotentielBrut() {
    return impactPotentielBrut;
  }

  public void setImpactPotentielBrut(ImpactPotentiel impactPotentielBrut) {
    this.impactPotentielBrut = impactPotentielBrut;
  }

  public Evolution getEvolutionRcp45Annee2035() {
    return evolutionRcp45Annee2035;
  }

  public void setEvolutionRcp45Annee2035(Evolution evolutionRcp45Annee2035) {
    this.evolutionRcp45Annee2035 = evolutionRcp45Annee2035;
  }

  public Evolution getEvolutionRcp45Annee2055() {
    return evolutionRcp45Annee2055;
  }

  public void setEvolutionRcp45Annee2055(Evolution evolutionRcp45Annee2055) {
    this.evolutionRcp45Annee2055 = evolutionRcp45Annee2055;
  }

  public Evolution getEvolutionRcp85Annee2035() {
    return evolutionRcp85Annee2035;
  }

  public void setEvolutionRcp85Annee2035(Evolution evolutionRcp85Annee2035) {
    this.evolutionRcp85Annee2035 = evolutionRcp85Annee2035;
  }

  public Evolution getEvolutionRcp85Annee2055() {
    return evolutionRcp85Annee2055;
  }

  public void setEvolutionRcp85Annee2055(Evolution evolutionRcp85Annee2055) {
    this.evolutionRcp85Annee2055 = evolutionRcp85Annee2055;
  }

  public Risque getRisqueBrutRcp45Annee2035() {
    return risqueBrutRcp45Annee2035;
  }

  public void setRisqueBrutRcp45Annee2035(Risque risqueBrutRcp45Annee2035) {
    this.risqueBrutRcp45Annee2035 = risqueBrutRcp45Annee2035;
  }

  public Risque getRisqueBrutRcp45Annee2055() {
    return risqueBrutRcp45Annee2055;
  }

  public void setRisqueBrutRcp45Annee2055(Risque risqueBrutRcp45Annee2055) {
    this.risqueBrutRcp45Annee2055 = risqueBrutRcp45Annee2055;
  }

  public Risque getRisqueBrutRcp85Annee2035() {
    return risqueBrutRcp85Annee2035;
  }

  public void setRisqueBrutRcp85Annee2035(Risque risqueBrutRcp85Annee2035) {
    this.risqueBrutRcp85Annee2035 = risqueBrutRcp85Annee2035;
  }

  public Risque getRisqueBrutRcp85Annee2055() {
    return risqueBrutRcp85Annee2055;
  }

  public void setRisqueBrutRcp85Annee2055(Risque risqueBrutRcp85Annee2055) {
    this.risqueBrutRcp85Annee2055 = risqueBrutRcp85Annee2055;
  }

  public HistoryEventSummary getCreation() {
    if (creation == null) {
      creation = new HistoryEventSummary();
    }
    return creation;
  }

  public void setCreation(HistoryEventSummary creation) {
    this.creation = creation;
  }

  public HistoryEventSummary getModification() {
    if (modification == null) {
      modification = new HistoryEventSummary();
    }
    return modification;
  }

  public void setModification(HistoryEventSummary modification) {
    this.modification = modification;
  }

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper().add("processu", getProcessus()).add("type", getType());
  }
}
