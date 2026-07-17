package sekoya.back.business.processus.model.atomic;

public enum ProcessusThematique {
  APPROVISIONNEMENTS("fa-dolly"),
  INFRASTRUCTURE_EQUIPEMENTS("fa-building"),
  OPERATIONS_PROCESSUS("fa-shapes"),
  ENERGIE_TELECOMS("fa-bolt"),
  EAU("fa-droplet"),
  LOGISTIQUE_TRANSPORTS("fa-truck"),
  DEMANDE("fa-store");

  private final String iconCssClass;

  ProcessusThematique(String iconCssClass) {
    this.iconCssClass = iconCssClass;
  }

  public String getIconCssClass() {
    return iconCssClass;
  }
}
