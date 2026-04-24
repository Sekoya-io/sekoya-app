package sekoya.back.security.model;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ANNOUNCEMENT_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ANNOUNCEMENT_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ROLE_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_ROLE_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_USER_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_USER_WRITE;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import java.util.List;
import java.util.Map;

public class SekoyaPermissionUtils {

  public static final Map<SekoyaPermissionCategoryEnum, List<String>> PERMISSIONS =
      new ImmutableSortedMap.Builder<SekoyaPermissionCategoryEnum, List<String>>(Ordering.natural())
          .put(
              SekoyaPermissionCategoryEnum.REFERENCE_DATA,
              List.of(GLOBAL_REFERENCE_DATA_READ, GLOBAL_REFERENCE_DATA_WRITE))
          .put(
              SekoyaPermissionCategoryEnum.ADMINISTRATION,
              List.of(
                  GLOBAL_ROLE_READ,
                  GLOBAL_ROLE_WRITE,
                  GLOBAL_ANNOUNCEMENT_READ,
                  GLOBAL_ANNOUNCEMENT_WRITE,
                  GLOBAL_USER_READ,
                  GLOBAL_USER_WRITE))
          .build();

  private SekoyaPermissionUtils() {}
}
