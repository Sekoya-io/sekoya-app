package sekoya.back.business.user.dao;

import java.util.List;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.common.model.EmailAddress;
import sekoya.back.business.user.model.User;

public interface IUserDao extends IGenericEntityDao<Long, User> {

  User getByUsernameCaseInsensitive(String username);

  User getByEmailCaseInsensitive(EmailAddress emailAddress);

  List<User> listByUsername(String username);
}
