package sekoya.front.common.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.iglooproject.functional.Joiners;

public final class CommonRenderers {

  private static final Renderer<Integer> KILO_EUROS =
      new Renderer<Integer>() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Integer value, Locale locale) {
          if (value == null) {
            return null;
          }
          return Joiners.onNonBreakingSpace()
              .join(value, getString("common.unit.kiloEuros", locale));
        }
      };

  public static Renderer<Integer> kiloEuros() {
    return KILO_EUROS;
  }

  private CommonRenderers() {}
}
