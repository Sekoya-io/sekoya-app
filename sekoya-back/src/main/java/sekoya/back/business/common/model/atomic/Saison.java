package sekoya.back.business.common.model.atomic;

public enum Saison {
  HIVER(1),
  PRINTEMPS(2),
  ETE(3),
  AUTOMNE(4);

  private final long idDrias;

  Saison(long idDrias) {
    this.idDrias = idDrias;
  }

  public long getIdDrias() {
    return idDrias;
  }
}
