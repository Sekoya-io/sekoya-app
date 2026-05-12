package sekoya.back.business.role.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.model.Role.RoleEnumKey;

public interface IRoleDao extends IGenericEntityDao<Long, Role> {

  Role getByEnumKey(RoleEnumKey enumKey);
}
