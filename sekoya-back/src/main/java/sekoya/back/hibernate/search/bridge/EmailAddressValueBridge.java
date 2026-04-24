package sekoya.back.hibernate.search.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import sekoya.back.business.common.model.EmailAddress;

public class EmailAddressValueBridge implements ValueBridge<EmailAddress, String> {

  @Override
  public String toIndexedValue(EmailAddress value, ValueBridgeToIndexedValueContext context) {
    if (value == null) {
      return null;
    }
    return value.getValue();
  }
}
