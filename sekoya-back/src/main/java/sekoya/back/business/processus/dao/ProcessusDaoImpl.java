package sekoya.back.business.processus.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.QProcessus;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.site.model.Site;

@Repository
public class ProcessusDaoImpl extends GenericEntityDaoImpl<Long, Processus>
    implements IProcessusDao {

  private static final QProcessus qProcessus = QProcessus.processus;

  @Override
  public Processus getBySiteAndType(Site site, ProcessusType type) {
    return new JPAQuery<Processus>(getEntityManager())
        .select(qProcessus)
        .from(qProcessus)
        .where(qProcessus.site.eq(site))
        .where(qProcessus.type.eq(type))
        .fetchOne();
  }
}
