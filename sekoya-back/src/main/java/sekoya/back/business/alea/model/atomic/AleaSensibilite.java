package sekoya.back.business.alea.model.atomic;

public enum AleaSensibilite {
  OPPORTUNITE(-1),
  TRES_FAIBLE(0),
  FAIBLE(1),
  MOYENNE(2),
  FORTE(3),
  TRES_FORTE(4);

  private final int score;

  AleaSensibilite(int score) {
    this.score = score;
  }

  public int getScore() {
    return score;
  }
}
