package sekoya.back.business.common.model.atomic;

public enum ImpactPotentiel implements IScore {
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

  @Override
  public int getScore() {
    return score;
  }

  @Override
  public String getRatingCssClass() {
    return "rating-badge-display-impact-potentiel";
  }

  public static ImpactPotentiel fromScore(int score) {
    for (ImpactPotentiel impactPotentiel : values()) {
      if (impactPotentiel.score == score) {
        return impactPotentiel;
      }
    }
    throw new IllegalArgumentException("Score d'impact potentiel inconnu : " + score);
  }
}
