package sekoya.back.business.common.model;

import java.math.BigDecimal;
import org.iglooproject.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;

public class Latitude extends AbstractMaterializedPrimitiveValue<BigDecimal, Latitude> {

  private static final long serialVersionUID = 1L;

  public static final int PRECISION = 8;
  public static final int SCALE = 6;

  public Latitude(BigDecimal value) {
    super(value);
  }
}
