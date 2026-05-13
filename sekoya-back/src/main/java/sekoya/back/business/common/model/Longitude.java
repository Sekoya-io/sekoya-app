package sekoya.back.business.common.model;

import java.math.BigDecimal;
import org.iglooproject.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;

public class Longitude extends AbstractMaterializedPrimitiveValue<BigDecimal, Longitude> {

  private static final long serialVersionUID = 1L;

  public static final int PRECISION = 9;
  public static final int SCALE = 6;

  public Longitude(BigDecimal value) {
    super(value);
  }
}
