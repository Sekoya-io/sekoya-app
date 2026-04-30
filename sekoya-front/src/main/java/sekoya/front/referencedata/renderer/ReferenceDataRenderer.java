package sekoya.front.referencedata.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import sekoya.back.business.referencedata.model.ReferenceData;

public abstract class ReferenceDataRenderer extends Renderer<ReferenceData<?>> {

  private static final long serialVersionUID = -3042035624376063917L;

  private static final Renderer<ReferenceData<?>> INSTANCE =
      new ReferenceDataRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(ReferenceData<?> value, Locale locale) {
          return value.getLabel();
        }
      }.nullsAsNull();

  public static final Renderer<ReferenceData<?>> get() {
    return INSTANCE;
  }

  private ReferenceDataRenderer() {}
}
