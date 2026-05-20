package sekoya.back.business.common.model.atomic;

public enum ImpactPotentiel {
  OPPORTUNITE(-1),
  MINEUR(0),
  SECONDAIRE(1),
  IMPORTANT(2),
  MAJEUR(3),
  CRITIQUE(4);

  private final int score;

  ImpactPotentiel(int score) {
    this.score = score;
  }

  public int getScore() {
    return score;
  }

  public static ImpactPotentiel fromScore(int score) {
    for (ImpactPotentiel impact : values()) {
      if (impact.score == score) {
        return impact;
      }
    }
    throw new IllegalArgumentException("Score d'impact potentiel inconnu : " + score);
  }
}
