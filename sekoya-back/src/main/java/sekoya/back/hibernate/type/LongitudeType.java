package sekoya.back.hibernate.type;

import java.math.BigDecimal;
import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedBigDecimalValueUserType;
import sekoya.back.business.common.model.Longitude;

public class LongitudeType extends AbstractImmutableMaterializedBigDecimalValueUserType<Longitude> {

  @Override
  public Class<Longitude> returnedClass() {
    return Longitude.class;
  }

  @Override
  protected Longitude instantiate(BigDecimal value) {
    return new Longitude(value);
  }

  @Override
  public int getDefaultSqlPrecision() {
    return Longitude.PRECISION;
  }

  @Override
  public int getDefaultSqlScale() {
    return Longitude.SCALE;
  }
}
