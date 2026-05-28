package sekoya.front.common.converter;

import java.util.Locale;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.iglooproject.spring.util.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class PointConverter extends AbstractConverter<Point> {

  private static final long serialVersionUID = 1L;

  private static final PointConverter INSTANCE = new PointConverter();

  public static PointConverter get() {
    return INSTANCE;
  }

  private static final GeometryFactory GF = new GeometryFactory(new PrecisionModel(), 4326);

  @Override
  public Point convertToObject(String value, Locale locale) throws ConversionException {
    String valueClean = StringUtils.trimAllWhitespace(value);

    if (!StringUtils.hasText(valueClean)) {
      return null;
    }

    String[] parts = value.split(",");

    if (parts.length != 2) {
      throw error(value, locale);
    }

    try {
      double lat = Double.parseDouble(parts[0].trim());
      double lon = Double.parseDouble(parts[1].trim());

      if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
        error(value, locale);
      }

      Point p = GF.createPoint(new Coordinate(lon, lat));
      p.setSRID(4326);
      return p;
    } catch (NumberFormatException _) {
      throw error(value, locale);
    }
  }

  private ConversionException error(String value, Locale locale) {
    return newConversionException("Invalid point format", value, locale)
        .setResourceKey("common.validator.point");
  }

  @Override
  public String convertToString(Point value, Locale locale) {
    if (value == null) {
      return null;
    }
    return value.getY() + ", " + value.getX();
  }

  @Override
  protected Class<Point> getTargetType() {
    return Point.class;
  }
}
