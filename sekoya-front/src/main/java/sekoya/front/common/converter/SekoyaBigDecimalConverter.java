package sekoya.front.common.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.BigDecimalConverter;
import org.danekja.java.util.function.serializable.SerializableUnaryOperator;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.util.convert.converters.CascadingConverter;

public final class SekoyaBigDecimalConverter extends CascadingConverter<BigDecimal> {

  private static final long serialVersionUID = 1L;

  private static final SerializableFunction2<Locale, DecimalFormatSymbols> SYMBOLS_DEFAULT =
      DecimalFormatSymbols::getInstance;

  private static final SerializableFunction2<Locale, DecimalFormatSymbols> SYMBOLS_FALLBACK =
      locale -> {
        DecimalFormatSymbols symbols = SYMBOLS_DEFAULT.apply(locale);
        symbols.setDecimalSeparator(symbols.getDecimalSeparator() == ',' ? '.' : ',');
        return symbols;
      };

  private static final SekoyaBigDecimalConverter INSTANCE = new SekoyaBigDecimalConverter();

  public static final IConverter<BigDecimal> get() {
    return INSTANCE;
  }

  public static final IConverter<BigDecimal> get(
      SerializableUnaryOperator<DecimalFormat> decimalFormatUnaryOperator) {
    return new SekoyaBigDecimalConverter(decimalFormatUnaryOperator);
  }

  private SekoyaBigDecimalConverter() {
    this(SerializableUnaryOperator.identity());
  }

  private SekoyaBigDecimalConverter(
      SerializableUnaryOperator<DecimalFormat> decimalFormatUnaryOperator) {
    super(
        new SilibBigDecimalInnerConverter(decimalFormatUnaryOperator, SYMBOLS_DEFAULT),
        List.of(),
        List.of(new SilibBigDecimalInnerConverter(decimalFormatUnaryOperator, SYMBOLS_FALLBACK)));
  }

  private static class SilibBigDecimalInnerConverter extends BigDecimalConverter {

    private static final long serialVersionUID = 1L;

    private final SerializableUnaryOperator<DecimalFormat> decimalFormatUnaryOperator;

    private final SerializableFunction2<Locale, DecimalFormatSymbols> decimalFormatSymbolsFunction;

    public SilibBigDecimalInnerConverter(
        SerializableUnaryOperator<DecimalFormat> decimalFormatUnaryOperator,
        SerializableFunction2<Locale, DecimalFormatSymbols> decimalFormatSymbolsFunction) {
      super();
      this.decimalFormatUnaryOperator = decimalFormatUnaryOperator;
      this.decimalFormatSymbolsFunction = decimalFormatSymbolsFunction;
    }

    @Override
    protected NumberFormat newNumberFormat(Locale locale) {
      DecimalFormat decimalFormat = (DecimalFormat) super.newNumberFormat(locale);
      decimalFormat.setDecimalFormatSymbols(decimalFormatSymbolsFunction.apply(locale));
      return decimalFormatUnaryOperator.apply(decimalFormat);
    }
  }
}
