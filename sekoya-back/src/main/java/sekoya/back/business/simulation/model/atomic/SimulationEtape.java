package sekoya.back.business.simulation.model.atomic;

public enum SimulationEtape {
  SITE,
  PROCESSUS,
  ALEA,
  IMPACT_POTENTIEL;

  public boolean isAfter(SimulationEtape etape) {
    return ordinal() > etape.ordinal();
  }

  public boolean isAfterOrCurrent(SimulationEtape etape) {
    return ordinal() >= etape.ordinal();
  }
}
