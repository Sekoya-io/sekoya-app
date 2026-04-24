package sekoya.back.hibernate.search.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import sekoya.back.business.common.model.PostalCode;

public class PostalCodeValueBridge implements ValueBridge<PostalCode, String> {

  @Override
  public String toIndexedValue(PostalCode value, ValueBridgeToIndexedValueContext context) {
    if (value == null) {
      return null;
    }
    return value.getValue();
  }
}
