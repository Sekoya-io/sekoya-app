package sekoya.front.common.behavior;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.ComponentTag;

public abstract class AjaxClickA11yEventBehavior extends AjaxEventBehavior {

  private static final long serialVersionUID = 1L;

  public AjaxClickA11yEventBehavior() {
    super("click keydown");
  }

  @Override
  protected void onComponentTag(ComponentTag tag) {
    super.onComponentTag(tag);
    if (!tag.getAttributes().containsKey("role")) {
      tag.put("role", "button");
    }
    if (!tag.getAttributes().containsKey("tabindex")) {
      tag.put("tabindex", "0");
    }
  }

  @Override
  protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
    super.updateAjaxAttributes(attributes);
    attributes
        .getAjaxCallListeners()
        .add(
            new AjaxCallListener()
                .onPrecondition(
                    """
                    var e = attrs.event;
                    if (e.target.closest('a, button')) {
                      return false;
                    }
                    if (e.type === 'keydown') {
                      if (e.key === 'Enter' || e.key === ' ' || e.key === 'Spacebar') {
                        e.preventDefault();
                        return true;
                      }
                      return false;
                    }
                    return true;
                    """));
  }
}
