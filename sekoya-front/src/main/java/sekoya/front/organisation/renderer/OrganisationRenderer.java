package sekoya.front.organisation.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import sekoya.back.business.organisation.model.Organisation;

public abstract class OrganisationRenderer extends Renderer<Organisation> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<Organisation> INSTANCE =
      new OrganisationRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Organisation value, Locale locale) {
          return value.getNom();
        }
      }.nullsAsNull();

  public static Renderer<Organisation> get() {
    return INSTANCE;
  }

  private OrganisationRenderer() {}
}
