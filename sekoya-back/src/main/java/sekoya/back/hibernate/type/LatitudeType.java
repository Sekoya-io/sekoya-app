package sekoya.back.hibernate.type;

import java.math.BigDecimal;
import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedBigDecimalValueUserType;
import sekoya.back.business.common.model.Latitude;

public class LatitudeType extends AbstractImmutableMaterializedBigDecimalValueUserType<Latitude> {

  @Override
  public Class<Latitude> returnedClass() {
    return Latitude.class;
  }

  @Override
  protected Latitude instantiate(BigDecimal value) {
    return new Latitude(value);
  }

  @Override
  public int getDefaultSqlPrecision() {
    return Latitude.PRECISION;
  }

  @Override
  public int getDefaultSqlScale() {
    return Latitude.SCALE;
  }
}
