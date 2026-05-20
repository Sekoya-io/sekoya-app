package sekoya.back.security.service.permission;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.user.model.User;

public interface ISitePermissionEvaluator extends IGenericPermissionEvaluator<User, Site> {

  boolean isVisible(User user, Site site);
}
