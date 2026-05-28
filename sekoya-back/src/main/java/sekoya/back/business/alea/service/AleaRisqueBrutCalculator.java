package sekoya.back.business.alea.service;

import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.ImpactPotentiel;
import sekoya.back.business.common.model.atomic.Risque;

public class AleaRisqueBrutCalculator {
  /**
   * Matrice du score de risque brut.
   *
   * <p>Indexation : MATRICE[impactPotentielBrut.ordinal][evolution.ordinal]
   *
   * <ul>
   *   <li>Lignes : OPPORTUNITE(-1), MINEUR(0), SECONDAIRE(1), IMPORTANT(2), MAJEUR(3), CRITIQUE(4)
   *   <li>Colonnes : FAVORABLE(-1), PAS_EVOLUTION(1), LEGEREMENT_DEFAVORABLE(2),
   *       MODEREMENT_DEFAVORABLE(3), DEFAVORABLE(4), FORTEMENT_DEFAVORABLE(5)
   * </ul>
   */
  private static final Integer[][] MATRICE = {
    {-1, -1, -1, -1, -1, -1},
    {0, 0, 0, 0, 0, 1},
    {0, 0, 0, 1, 2, 2},
    {0, 1, 1, 2, 2, 3},
    {1, 2, 2, 3, 3, 4},
    {2, 2, 3, 3, 4, 4},
  };

  private AleaRisqueBrutCalculator() {}

  public static Risque generer(ImpactPotentiel impactPotentielBrut, Evolution evolution) {
    if (impactPotentielBrut == null || evolution == null) {
      return null;
    }
    return Risque.fromScore(MATRICE[impactPotentielBrut.ordinal()][evolution.ordinal()]);
  }
}
