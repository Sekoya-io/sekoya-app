package sekoya.back.business.role.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.role.model.QRole;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.model.Role.RoleEnumKey;

@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<Long, Role> implements IRoleDao {
  private static final QRole qRole = QRole.role;

  @Override
  public Role getByEnumKey(RoleEnumKey enumKey) {
    return new JPAQuery<>(getEntityManager())
        .select(qRole)
        .from(qRole)
        .where(qRole.enumKey.eq(enumKey))
        .fetchOne();
  }
}
