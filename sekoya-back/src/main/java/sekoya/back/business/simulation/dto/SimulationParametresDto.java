package sekoya.back.business.simulation.dto;

import java.io.Serializable;
import org.bindgen.Bindable;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;

@Bindable
public class SimulationParametresDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Scenario scenario;

  private Horizon horizon;

  private boolean enableProcessus;

  public SimulationParametresDto() {
    super();
    this.scenario = Scenario.RCP_4_5;
    this.horizon = Horizon.ANNEE_2055;
    this.enableProcessus = false;
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

  public boolean isEnableProcessus() {
    return enableProcessus;
  }

  public void setEnableProcessus(boolean enableProcessus) {
    this.enableProcessus = enableProcessus;
  }
}
