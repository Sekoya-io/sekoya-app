package sekoya.back.security.model;

import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Field;
import java.util.Collection;
import org.iglooproject.jpa.security.model.NamedPermission;

public final class SekoyaPermission extends NamedPermission {

  private static final long serialVersionUID = 8541973919257428300L;

  public static final Collection<SekoyaPermission> ALL;

  static {
    ImmutableSet.Builder<SekoyaPermission> builder = ImmutableSet.builder();
    Field[] fields = SekoyaPermissionConstants.class.getFields();
    for (Field field : fields) {
      try {
        Object fieldValue = field.get(null);
        if (fieldValue instanceof String) {
          builder.add(new SekoyaPermission((String) fieldValue));
        }
      } catch (IllegalArgumentException | IllegalAccessException ignored) { // NOSONAR
      }
    }
    ALL = builder.build();
  }

  private SekoyaPermission(String name) {
    super(name);
  }
}
