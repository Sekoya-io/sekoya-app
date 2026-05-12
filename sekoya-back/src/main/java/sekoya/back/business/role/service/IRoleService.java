package sekoya.back.business.role.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.model.Role.RoleEnumKey;

public interface IRoleService extends IGenericEntityService<Long, Role> {

  Role getByEnumKey(RoleEnumKey enumKey);
}
