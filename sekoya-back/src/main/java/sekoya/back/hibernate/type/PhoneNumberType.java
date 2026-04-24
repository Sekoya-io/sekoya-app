package sekoya.back.hibernate.type;

import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;
import sekoya.back.business.common.model.PhoneNumber;

public class PhoneNumberType extends AbstractImmutableMaterializedStringValueUserType<PhoneNumber> {

  @Override
  public Class<PhoneNumber> returnedClass() {
    return PhoneNumber.class;
  }

  @Override
  protected PhoneNumber instantiate(String value) {
    return PhoneNumber.buildClean(value);
  }
}
