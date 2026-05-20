package sekoya.front.processus.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import sekoya.back.business.processus.model.Processus;

public abstract class ProcessusRenderer extends Renderer<Processus> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<Processus> INSTANCE =
      new ProcessusRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Processus value, Locale locale) {
          return value.getNom();
        }
      }.nullsAsNull();

  public static Renderer<Processus> get() {
    return INSTANCE;
  }

  private ProcessusRenderer() {}
}
