package sekoya.back.security.service.permission;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.user.model.User;

public interface IProcessusPermissionEvaluator
    extends IGenericPermissionEvaluator<User, Processus> {

  boolean isVisible(User user, Processus processus);
}
