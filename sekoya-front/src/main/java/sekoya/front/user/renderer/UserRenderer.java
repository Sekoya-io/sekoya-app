package sekoya.front.user.renderer;

import com.google.common.collect.Iterables;
import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.iglooproject.functional.Joiners;
import sekoya.back.business.user.model.User;
import sekoya.front.common.renderer.RoleRenderer;

public abstract class UserRenderer extends Renderer<User> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<User> INSTANCE =
      new UserRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(User value, Locale locale) {
          return value.getFullName();
        }
      }.nullsAsNull();

  private static final Renderer<User> ROLES =
      new UserRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(User value, Locale locale) {
          return Joiners.onNewLine()
              .join(Iterables.transform(value.getRoles(), RoleRenderer.get().asFunction(locale)));
        }
      }.nullsAsNull();

  public static Renderer<User> get() {
    return INSTANCE;
  }

  public static Renderer<User> roles() {
    return ROLES;
  }

  private UserRenderer() {}
}
