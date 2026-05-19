package sekoya.front.common.converter;

import java.math.BigDecimal;
import java.util.Locale;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import sekoya.back.business.common.model.Latitude;

public final class LatitudeConverter extends AbstractConverter<Latitude> {

  private static final long serialVersionUID = 1L;

  private static final LatitudeConverter INSTANCE = new LatitudeConverter();

  private static final IConverter<BigDecimal> BIG_DECIMAL_CONVERTER =
      SekoyaBigDecimalConverter.get(
          decimalFormat -> {
            decimalFormat.setMinimumIntegerDigits(1);
            decimalFormat.setMinimumFractionDigits(Latitude.SCALE);
            decimalFormat.setMaximumFractionDigits(Latitude.SCALE);
            return decimalFormat;
          });

  public static LatitudeConverter get() {
    return INSTANCE;
  }

  @Override
  public Latitude convertToObject(String value, Locale locale) throws ConversionException {
    BigDecimal object = BIG_DECIMAL_CONVERTER.convertToObject(value, locale);

    try {
      object = object.setScale(Latitude.SCALE);
    } catch (ArithmeticException e) { // NOSONAR: e is not propagated
      throw newConversionException("Invalid format latitude", value, locale);
    }

    if (object.precision() > Latitude.PRECISION) {
      throw newConversionException("Invalid format latitude", value, locale);
    }

    //    if (!LatitudeValidator.getInstance().isValid(object)) {
    //      throw newConversionException("Invalid format latitude", value, locale);
    //    }

    return new Latitude(object);
  }

  @Override
  public String convertToString(Latitude value, Locale locale) {
    if (value == null || value.getValue() == null) {
      return null;
    }
    return BIG_DECIMAL_CONVERTER.convertToString(value.getValue(), locale);
  }

  @Override
  protected Class<Latitude> getTargetType() {
    return Latitude.class;
  }
}
