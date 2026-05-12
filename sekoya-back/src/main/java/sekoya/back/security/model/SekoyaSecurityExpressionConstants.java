package sekoya.back.security.model;

import static org.iglooproject.commons.util.security.PermissionObject.DEFAULT_PERMISSION_OBJECT_NAME;

public final class SekoyaSecurityExpressionConstants {

  public static final String USER_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_WRITE
          + "')";

  public static final String USER_ORGANISATION_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_ORGANISATION_WRITE
          + "')";

  public static final String USER_ADMINISTRATEUR_FONCTIONNEL_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_ADMINISTATEUR_FONCTIONNEL_WRITE
          + "')";

  public static final String USER_ADMINISTRATEUR_TECHNIQUE_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_ADMINISTATEUR_TECHNIQUE_WRITE
          + "')";

  public static final String USER_ENABLE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_ENABLE
          + "')";

  public static final String USER_DISABLE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_DISABLE
          + "')";

  public static final String USER_EDIT_PASSWORD =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_EDIT_PASSWORD
          + "')";

  public static final String USER_RECOVERY_PASSWORD =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_RECOVERY_PASSWORD
          + "')";

  public static final String ADMIN_EDIT_PASSWORD =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.ADMIN_EDIT_PASSWORD
          + "')";

  public static final String ADMIN_RECOVERY_PASSWORD =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.ADMIN_RECOVERY_PASSWORD
          + "')";

  public static final String USER_OPEN_ANNONCEMENT =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_OPEN_ANNONCEMENT
          + "')";

  public static final String USER_CLOSE_ANNONCEMENT =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.USER_CLOSE_ANNONCEMENT
          + "')";

  public static final String ANNOUNCEMENT_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.ANNOUNCEMENT_WRITE
          + "')";
  public static final String ANNOUNCEMENT_REMOVE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + SekoyaPermissionConstants.ANNOUNCEMENT_REMOVE
          + "')";

  private SekoyaSecurityExpressionConstants() {}
}
