package sekoya.back.business.referencedata.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.model.QCommune;
import sekoya.back.business.referencedata.model.atomic.CommuneTypeInsee;

@Repository
public class CommuneDaoImpl extends GenericEntityDaoImpl<Long, Commune> implements ICommuneDao {

  private static final QCommune qCommune = QCommune.commune;

  @Override
  public Commune getByCodeInsee(String codeInsee) {
    Commune commune = null;
    commune =
        new JPAQuery<Commune>(getEntityManager())
            .select(qCommune)
            .from(qCommune)
            .where(qCommune.codeInsee.eq(codeInsee))
            .where(qCommune.typeInsee.in(CommuneTypeInsee.principaux()))
            .orderBy(qCommune.id.asc())
            .fetchFirst();

    if (commune == null) {
      commune =
          new JPAQuery<Commune>(getEntityManager())
              .select(qCommune)
              .from(qCommune)
              .where(qCommune.codeInsee.eq(codeInsee))
              .where(qCommune.typeInsee.in(CommuneTypeInsee.secondaires()))
              .orderBy(qCommune.id.asc())
              .fetchFirst();
    }

    return commune;
  }
}
