package sekoya.back.hibernate.type;

import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;
import sekoya.back.business.common.model.CodePostal;

public class CodePostalType extends AbstractImmutableMaterializedStringValueUserType<CodePostal> {

  @Override
  public Class<CodePostal> returnedClass() {
    return CodePostal.class;
  }

  @Override
  protected CodePostal instantiate(String value) {
    return new CodePostal(value);
  }

  @Override
  public long getDefaultSqlLength() {
    return 5L;
  }
}
