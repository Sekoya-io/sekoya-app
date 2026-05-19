package sekoya.back.business.organisation.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.organisation.model.QOrganisation;

@Repository
public class OrganisationDaoImpl extends GenericEntityDaoImpl<Long, Organisation>
    implements IOrganisationDao {

  private static final QOrganisation qOrganisation = QOrganisation.organisation;

  @Override
  public Organisation getDefault() {
    return new JPAQuery<Organisation>(getEntityManager())
        .from(qOrganisation)
        .orderBy(qOrganisation.nom.asc())
        .orderBy(qOrganisation.id.asc())
        .fetchFirst();
  }

  @Override
  public Organisation getByNomCaseInsensitive(String nom) {
    return new JPAQuery<Organisation>(getEntityManager())
        .from(qOrganisation)
        .where(qOrganisation.nom.lower().eq(nom.toLowerCase()))
        .fetchOne();
  }
}
