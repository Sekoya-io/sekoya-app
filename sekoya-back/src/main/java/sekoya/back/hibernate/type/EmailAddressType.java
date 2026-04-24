package sekoya.back.hibernate.type;

import org.iglooproject.jpa.hibernate.usertype.AbstractImmutableMaterializedStringValueUserType;
import sekoya.back.business.common.model.EmailAddress;

public class EmailAddressType
    extends AbstractImmutableMaterializedStringValueUserType<EmailAddress> {

  @Override
  public Class<EmailAddress> returnedClass() {
    return EmailAddress.class;
  }

  @Override
  protected EmailAddress instantiate(String value) {
    return new EmailAddress(value);
  }
}
