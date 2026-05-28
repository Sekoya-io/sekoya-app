package sekoya.back.business.common.model.atomic;

public enum Risque {
  OPPORTUNITE(-1),
  FAIBLE(0),
  MODERE(1),
  IMPORTANT(2),
  MAJEUR(3),
  CRITIQUE(4);

  private final int score;

  Risque(int score) {
    this.score = score;
  }

  public int getScore() {
    return score;
  }

  public static Risque fromScore(int score) {
    for (Risque risque : values()) {
      if (risque.score == score) {
        return risque;
      }
    }
    throw new IllegalArgumentException("Score de risque inconnu : " + score);
  }
}
