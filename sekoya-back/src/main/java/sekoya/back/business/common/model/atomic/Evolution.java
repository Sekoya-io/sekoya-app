package sekoya.back.business.common.model.atomic;

public enum Evolution {
  FAVORABLE(-1, Risque.OPPORTUNITE),
  PAS_EVOLUTION(1, Risque.FAIBLE),
  LEGEREMENT_DEFAVORABLE(2, Risque.MODERE),
  MODEREMENT_DEFAVORABLE(3, Risque.IMPORTANT),
  DEFAVORABLE(4, Risque.MAJEUR),
  FORTEMENT_DEFAVORABLE(5, Risque.CRITIQUE);

  private final int score;

  private final Risque risque;

  Evolution(int score, Risque risque) {
    this.score = score;
    this.risque = risque;
  }

  public int getScore() {
    return score;
  }

  public Risque getRisque() {
    return risque;
  }

  public static Evolution fromScore(int score) {
    for (Evolution evolution : values()) {
      if (evolution.score == score) {
        return evolution;
      }
    }
    throw new IllegalArgumentException("Score d'évolution inconnu : " + score);
  }
}
