package sekoya.back.business.alea.service;

import sekoya.back.business.alea.model.atomic.AleaSensibilite;
import sekoya.back.business.common.model.atomic.ImpactPotentiel;
import sekoya.back.business.processus.model.atomic.ProcessusPriorite;

public class AleaImpactPotentielBrutCalculator {
  /**
   * Matrice du score d'impact potentiel brut.
   *
   * <p>Indexation : MATRICE[priorite.score][sensibilite.score + 1]
   *
   * <ul>
   *   <li>Lignes : MINEUR(0), SECONDAIRE(1), IMPORTANT(2), MAJEUR(3), VITAL(4)
   *   <li>Colonnes : OPPORTUNITE(-1), TRES_FAIBLE(0), FAIBLE(1), MOYENNE(2), FORTE(3),
   *       TRES_FORTE(4)
   * </ul>
   */
  private static final int[][] MATRICE = {
    {-1, 0, 0, 0, 0, 1},
    {-1, 0, 0, 1, 1, 2},
    {-1, 0, 1, 2, 2, 3},
    {-1, 0, 1, 2, 3, 4},
    {-1, 1, 2, 3, 4, 4}
  };

  private AleaImpactPotentielBrutCalculator() {}

  public static ImpactPotentiel generer(ProcessusPriorite priorite, AleaSensibilite sensibilite) {
    if (priorite == null || sensibilite == null) {
      return null;
    }
    return ImpactPotentiel.fromScore(MATRICE[priorite.getScore()][sensibilite.getScore() + 1]);
  }
}
