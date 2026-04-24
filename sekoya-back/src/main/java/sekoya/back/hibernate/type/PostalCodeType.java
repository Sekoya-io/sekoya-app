package sekoya.back.hibernate.type;

import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;
import sekoya.back.business.common.model.PostalCode;

public class PostalCodeType extends AbstractImmutableMaterializedStringValueUserType<PostalCode> {

  @Override
  public Class<PostalCode> returnedClass() {
    return PostalCode.class;
  }

  @Override
  protected PostalCode instantiate(String value) {
    return new PostalCode(value);
  }
}
