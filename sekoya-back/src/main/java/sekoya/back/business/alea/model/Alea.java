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
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import sekoya.back.business.alea.model.atomic.AleaSensibilite;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.ImpactPotentiel;
import sekoya.back.business.processus.model.Processus;

@Entity
@Bindable
@Cacheable
@Indexed
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"processus_id", "type"}))
public class Alea extends GenericEntity<Long, Alea> {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Processus processus;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private AleaType type;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private AleaSensibilite sensibilite;

  @Basic(optional = false)
  @Enumerated(EnumType.STRING)
  private ImpactPotentiel impactPotentielBrut;

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
