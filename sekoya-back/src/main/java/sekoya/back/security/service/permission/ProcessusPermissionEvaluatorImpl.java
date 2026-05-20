package sekoya.back.security.service.permission;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_PROCESSUS_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_PROCESSUS_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_DISABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_ENABLE;
import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_READ;
import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_WRITE;

import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.user.model.User;

@Service
public class ProcessusPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<Processus>
    implements IProcessusPermissionEvaluator {

  private final ISitePermissionEvaluator sitePermissionEvaluator;

  public ProcessusPermissionEvaluatorImpl(ISitePermissionEvaluator sitePermissionEvaluator) {
    super();
    this.sitePermissionEvaluator = sitePermissionEvaluator;
  }

  @Override
  public boolean hasPermission(User user, Processus processus, Permission permission) {
    if (is(permission, PROCESSUS_READ)) {
      return canReadProcessus(user, processus);
    } else if (is(permission, PROCESSUS_WRITE)) {
      return canWriteProcessus(user, processus);
    } else if (is(permission, PROCESSUS_ENABLE)) {
      return canEnableProcessus(user, processus);
    } else if (is(permission, PROCESSUS_DISABLE)) {
      return canDisableProcessus(user, processus);
    }
    return false;
  }

  @Override
  public boolean isVisible(User user, Processus processus) {
    return sitePermissionEvaluator.isVisible(user, processus.getSite());
  }

  public boolean canReadProcessus(User user, Processus processus) {
    return hasPermission(user, GLOBAL_PROCESSUS_READ) && isVisible(user, processus);
  }

  public boolean canWriteProcessus(User user, Processus processus) {
    return hasPermission(user, GLOBAL_PROCESSUS_WRITE) && isVisible(user, processus);
  }

  public boolean canEnableProcessus(User user, Processus processus) {
    return !processus.isEnabled()
        && hasPermission(user, GLOBAL_PROCESSUS_WRITE)
        && isVisible(user, processus);
  }

  public boolean canDisableProcessus(User user, Processus processus) {
    return processus.isEnabled()
        && hasPermission(user, GLOBAL_PROCESSUS_WRITE)
        && isVisible(user, processus);
  }
}
