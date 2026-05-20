package sekoya.front.processus.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.alea.model.Alea;

public abstract class AleaRenderer extends Renderer<Alea> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<Alea> INSTANCE =
      new AleaRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Alea value, Locale locale) {
          return EnumRenderer.get().render(value.getType(), locale);
        }
      }.nullsAsNull();

  public static Renderer<Alea> get() {
    return INSTANCE;
  }

  private AleaRenderer() {}
}
