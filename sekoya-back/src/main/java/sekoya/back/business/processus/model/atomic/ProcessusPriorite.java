package sekoya.back.business.processus.model.atomic;

import sekoya.back.business.common.model.atomic.IScore;

public enum ProcessusPriorite implements IScore {
  MINEUR(0, 0),
  SECONDAIRE(1, 1),
  IMPORTANT(2, 1),
  MAJEUR(3, 20),
  VITAL(4, 30);

  private final int score;
  private final int pourcentageChiffreAffaires;

  ProcessusPriorite(int score, int pourcentageChiffreAffaires) {
    this.score = score;
    this.pourcentageChiffreAffaires = pourcentageChiffreAffaires;
  }

  @Override
  public int getScore() {
    return score;
  }

  @Override
  public String getRatingCssClass() {
    return "rating-badge-display-processus-priorite";
  }

  public int getPourcentageChiffreAffaires() {
    return pourcentageChiffreAffaires;
  }
}
