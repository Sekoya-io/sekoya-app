package sekoya.front.common.renderer;

import static org.iglooproject.spring.util.StringUtils.emptyTextToNull;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.iglooproject.functional.Joiners;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import sekoya.front.history.renderer.IHistoryValueRenderer;

public abstract class HistoryEventSummaryRenderer extends Renderer<HistoryEventSummary> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<HistoryEventSummary> DATE =
      new HistoryEventSummaryRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(HistoryEventSummary value, Locale locale) {
          return InstantRenderer.get().render(value.getDate(), locale);
        }
      }.nullsAsNull();

  private static final Renderer<HistoryEventSummary> SUBJECT =
      new HistoryEventSummaryRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(HistoryEventSummary value, Locale locale) {
          return IHistoryValueRenderer.get().render(value.getSubject(), locale);
        }
      }.nullsAsNull();

  private static final Renderer<HistoryEventSummary> COMPLET =
      new HistoryEventSummaryRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(HistoryEventSummary value, Locale locale) {
          if (value.getSubject() == null) {
            return HistoryEventSummaryRenderer.date().render(value, locale);
          } else {
            return Joiners.onMiddotSpace()
                .join(
                    emptyTextToNull(HistoryEventSummaryRenderer.date().render(value, locale)),
                    emptyTextToNull(HistoryEventSummaryRenderer.subject().render(value, locale)));
          }
        }
      }.nullsAsNull();

  public static Renderer<HistoryEventSummary> date() {
    return DATE;
  }

  public static Renderer<HistoryEventSummary> subject() {
    return SUBJECT;
  }

  public static Renderer<HistoryEventSummary> complet() {
    return COMPLET;
  }

  private HistoryEventSummaryRenderer() {}
}
