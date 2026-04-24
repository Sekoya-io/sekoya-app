package sekoya.back.business.role.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.role.model.Role;

public interface IRoleDao extends IGenericEntityDao<Long, Role> {

  Role getByTitle(String title);
}
