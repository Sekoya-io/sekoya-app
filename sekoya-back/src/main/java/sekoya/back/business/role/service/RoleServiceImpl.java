package sekoya.back.business.role.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sekoya.back.business.role.dao.IRoleDao;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.model.Role.RoleEnumKey;

@Service
public class RoleServiceImpl extends GenericEntityServiceImpl<Long, Role> implements IRoleService {

  private final IRoleDao dao;

  @Autowired
  public RoleServiceImpl(IRoleDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public Role getByEnumKey(RoleEnumKey enumKey) {
    return dao.getByEnumKey(enumKey);
  }
}
