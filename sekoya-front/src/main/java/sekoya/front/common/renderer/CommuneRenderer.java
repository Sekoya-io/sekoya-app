package sekoya.front.common.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.functional.Joiners;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.front.referencedata.renderer.ReferenceDataRenderer;

public abstract class CommuneRenderer extends Renderer<Commune> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<Commune> INSTANCE =
      new CommuneRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Commune value, Locale locale) {
          return Joiners.onSpace()
              .join(
                  ReferenceDataRenderer.get().render(value, locale),
                  new StringResourceModel("common.parentheses")
                      .setParameters(Joiners.onComma().skipNulls().join(value.getCodesPostaux()))
                      .getObject());
        }
      }.nullsAsNull();

  public static final Renderer<Commune> get() {
    return INSTANCE;
  }

  private CommuneRenderer() {}
}
