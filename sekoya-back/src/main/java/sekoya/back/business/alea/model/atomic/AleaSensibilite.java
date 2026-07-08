package sekoya.back.business.alea.model.atomic;

import sekoya.back.business.common.model.atomic.IScore;

public enum AleaSensibilite implements IScore {
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

  @Override
  public int getScore() {
    return score;
  }

  @Override
  public String getRatingCssClass() {
    return "rating-badge-display-alea-sensibilite";
  }
}
