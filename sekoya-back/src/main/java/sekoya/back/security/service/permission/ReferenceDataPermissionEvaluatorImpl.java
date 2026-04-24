package sekoya.back.security.service.permission;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.REFERENCE_DATA_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.REFERENCE_DATA_WRITE;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import sekoya.back.business.referencedata.model.ReferenceData;
import sekoya.back.business.referencedata.predicate.ReferenceDataPredicates;
import sekoya.back.business.user.model.User;

@Service
public class ReferenceDataPermissionEvaluatorImpl
    extends AbstractGenericPermissionEvaluator<ReferenceData<?>>
    implements IReferenceDataPermissionEvaluator {

  @Override
  public boolean hasPermission(User user, ReferenceData<?> referenceData, Permission permission) {
    if (is(permission, REFERENCE_DATA_READ)) {
      return canRead(user);
    } else if (is(permission, REFERENCE_DATA_WRITE)) {
      return canWrite(user, referenceData);
    }
    return false;
  }

  @VisibleForTesting
  public boolean canRead(User user) {
    return hasPermission(user, GLOBAL_REFERENCE_DATA_READ);
  }

  @VisibleForTesting
  public boolean canWrite(User user, ReferenceData<?> referenceData) {
    if (!referenceData.isNew()
        && ReferenceDataPredicates.editable().negate().apply(referenceData)) {
      return false;
    }

    return canRead(user) && hasPermission(user, GLOBAL_REFERENCE_DATA_WRITE);
  }
}
