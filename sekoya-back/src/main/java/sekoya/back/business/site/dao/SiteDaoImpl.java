package sekoya.back.business.site.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.site.model.QSite;
import sekoya.back.business.site.model.Site;

@Repository
public class SiteDaoImpl extends GenericEntityDaoImpl<Long, Site> implements ISiteDao {

  private static final QSite qSite = QSite.site;

  @Override
  public Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom) {
    return new JPAQuery<Site>(getEntityManager())
        .from(qSite)
        .where(qSite.organisation.eq(organisation))
        .where(qSite.nom.lower().eq(nom.toLowerCase()))
        .fetchOne();
  }
}
