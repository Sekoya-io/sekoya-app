package sekoya.back.hibernate.search.bridge;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class EnumOrdinalValueBridge implements ValueBridge<Enum, Integer> {

  @Override
  public Integer toIndexedValue(Enum value, ValueBridgeToIndexedValueContext context) {
    if (value == null) {
      return null;
    }
    return value.ordinal();
  }
}
