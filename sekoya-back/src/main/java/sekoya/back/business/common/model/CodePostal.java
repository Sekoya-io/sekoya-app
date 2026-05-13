package sekoya.back.business.common.model;

import org.bindgen.Bindable;
import org.iglooproject.jpa.hibernate.usertype.AbstractMaterializedPrimitiveValue;

@Bindable
public class CodePostal extends AbstractMaterializedPrimitiveValue<String, CodePostal> {

  private static final long serialVersionUID = 1L;

  public CodePostal(String value) {
    super(value);
  }
}
