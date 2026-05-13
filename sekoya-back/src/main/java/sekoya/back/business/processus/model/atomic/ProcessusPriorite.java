package sekoya.back.business.processus.model.atomic;

public enum ProcessusPriorite {
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

  public int getScore() {
    return score;
  }

  public int getPourcentageChiffreAffaires() {
    return pourcentageChiffreAffaires;
  }
}
