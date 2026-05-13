package sekoya.back.hibernate.search.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import sekoya.back.business.common.model.CodePostal;

public class CodePostalValueBridge implements ValueBridge<CodePostal, String> {

  @Override
  public String toIndexedValue(CodePostal value, ValueBridgeToIndexedValueContext context) {
    if (value == null) {
      return null;
    }
    return value.getValue();
  }
}
