package sekoya.back.business.role.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.role.model.Role;

@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<Long, Role> implements IRoleDao {}
