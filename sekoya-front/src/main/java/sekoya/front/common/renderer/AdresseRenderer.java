package sekoya.front.common.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.iglooproject.functional.Joiners;
import org.iglooproject.spring.util.StringUtils;
import sekoya.back.business.common.model.embeddable.Adresse;
import sekoya.front.common.converter.CodePostalConverter;
import sekoya.front.referencedata.renderer.ReferenceDataRenderer;

public abstract class AdresseRenderer extends Renderer<Adresse> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<Adresse> INSTANCE =
      new AdresseRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Adresse value, Locale locale) {
          return Joiners.onNewLine()
              .join(
                  StringUtils.emptyTextToNull(value.getAdresse1()),
                  StringUtils.emptyTextToNull(value.getAdresse2()),
                  StringUtils.emptyTextToNull(
                      Joiners.onSpace()
                          .join(
                              StringUtils.emptyTextToNull(
                                  CodePostalConverter.get()
                                      .convertToString(value.getCodePostal(), locale)),
                              StringUtils.emptyTextToNull(
                                  ReferenceDataRenderer.get()
                                      .render(value.getCommune(), locale)))));
        }
      }.nullsAsNull();

  public static Renderer<Adresse> get() {
    return INSTANCE;
  }

  private AdresseRenderer() {}
}
