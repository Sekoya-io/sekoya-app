package sekoya.front.common.converter;

import java.math.BigDecimal;
import java.util.Locale;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import sekoya.back.business.common.model.Longitude;

public final class LongitudeConverter extends AbstractConverter<Longitude> {

  private static final long serialVersionUID = 1L;

  private static final LongitudeConverter INSTANCE = new LongitudeConverter();

  private static final IConverter<BigDecimal> BIG_DECIMAL_CONVERTER =
      SekoyaBigDecimalConverter.get(
          decimalFormat -> {
            decimalFormat.setMinimumIntegerDigits(1);
            decimalFormat.setMinimumFractionDigits(Longitude.SCALE);
            decimalFormat.setMaximumFractionDigits(Longitude.SCALE);
            return decimalFormat;
          });

  public static LongitudeConverter get() {
    return INSTANCE;
  }

  @Override
  public Longitude convertToObject(String value, Locale locale) throws ConversionException {
    BigDecimal object = BIG_DECIMAL_CONVERTER.convertToObject(value, locale);

    try {
      object = object.setScale(Longitude.SCALE);
    } catch (ArithmeticException e) { // NOSONAR: e is not propagated
      throw newConversionException("Invalid format longitude", value, locale);
    }

    if (object.precision() > Longitude.PRECISION) {
      throw newConversionException("Invalid format longitude", value, locale);
    }

    //    if (!LongitudeValidator.getInstance().isValid(object)) {
    //      throw newConversionException("Invalid format longitude", value, locale);
    //    }

    return new Longitude(object);
  }

  @Override
  public String convertToString(Longitude value, Locale locale) {
    if (value == null || value.getValue() == null) {
      return null;
    }
    return BIG_DECIMAL_CONVERTER.convertToString(value.getValue(), locale);
  }

  @Override
  protected Class<Longitude> getTargetType() {
    return Longitude.class;
  }
}
