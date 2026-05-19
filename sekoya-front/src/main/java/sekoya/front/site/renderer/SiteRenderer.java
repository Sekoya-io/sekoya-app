package sekoya.front.site.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import sekoya.back.business.site.model.Site;

public abstract class SiteRenderer extends Renderer<Site> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<Site> INSTANCE =
      new SiteRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Site value, Locale locale) {
          return value.getNom();
        }
      }.nullsAsNull();

  public static Renderer<Site> get() {
    return INSTANCE;
  }

  private SiteRenderer() {}
}
