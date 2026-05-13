package sekoya.front.common.behavior;

import org.apache.wicket.behavior.AttributeAppender;

public class FormCancelDefaultSubmitBehavior extends AttributeAppender {

  private static final long serialVersionUID = 1L;

  public FormCancelDefaultSubmitBehavior() {
    super("onsubmit", "return false;");
  }
}
