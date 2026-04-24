package sekoya.back.business.common.model.comparator;

import java.util.Comparator;
import java.util.Locale;
import org.iglooproject.jpa.more.business.localization.util.AbstractLocalizedTextComparator;
import sekoya.back.business.common.model.embeddable.LocalizedText;
import sekoya.back.business.common.util.SekoyaLocale;

public class LocalizedTextComparator extends AbstractLocalizedTextComparator<LocalizedText> {

  private static final long serialVersionUID = -1217040817920839219L;

  private static final LocalizedTextComparator INSTANCE =
      new LocalizedTextComparator(SekoyaLocale.DEFAULT, SekoyaLocale.comparator());

  public LocalizedTextComparator(Locale locale) {
    super(locale);
  }

  public LocalizedTextComparator(Locale locale, Comparator<String> comparator) {
    super(locale, comparator);
  }

  public static final LocalizedTextComparator get() {
    return INSTANCE;
  }
}
