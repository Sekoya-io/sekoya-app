package sekoya.back.business.simulation.dto;

import java.io.Serializable;
import org.bindgen.Bindable;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;

@Bindable
public class SimulationSearchDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Scenario scenario;

  private Horizon horizon;

  private boolean applyProcessus;

  public SimulationSearchDto() {
    super();
    this.scenario = Scenario.RCP_4_5;
    this.horizon = Horizon.ANNEE_2055;
    this.applyProcessus = true;
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

  public boolean isApplyProcessus() {
    return applyProcessus;
  }

  public void setApplyProcessus(boolean applyProcessus) {
    this.applyProcessus = applyProcessus;
  }
}
