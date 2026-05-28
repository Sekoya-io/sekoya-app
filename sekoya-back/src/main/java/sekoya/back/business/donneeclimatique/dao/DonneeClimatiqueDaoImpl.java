package sekoya.back.business.donneeclimatique.dao;

import com.querydsl.jpa.impl.JPAQuery;
import java.util.Comparator;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.donneeclimatique.model.DonneeClimatique;
import sekoya.back.business.donneeclimatique.model.PointGeographique;
import sekoya.back.business.donneeclimatique.model.QDonneeClimatique;

@Repository
public class DonneeClimatiqueDaoImpl extends GenericEntityDaoImpl<Long, DonneeClimatique>
    implements IDonneeClimatiqueDao {

  private static final QDonneeClimatique qDonneeClimatique = QDonneeClimatique.donneeClimatique;

  @Override
  public DonneeClimatique getByAlea(Alea alea, Scenario scenario, Horizon horizon) {
    return new JPAQuery<>(getEntityManager())
        .select(qDonneeClimatique)
        .from(qDonneeClimatique)
        .where(
            qDonneeClimatique.pointGeographique.eq(
                alea.getProcessus().getSite().getPointGeographique()))
        .where(qDonneeClimatique.aleaType.eq(alea.getType()))
        .where(qDonneeClimatique.scenario.eq(scenario))
        .where(qDonneeClimatique.horizon.eq(horizon))
        .fetchOne();
  }

  @Override
  public DonneeClimatique getPlusDefavorableByPointGeographique(
      PointGeographique pointGeographique, Scenario scenario, Horizon horizon) {
    return new JPAQuery<>(getEntityManager())
            .select(qDonneeClimatique)
            .from(qDonneeClimatique)
            .where(qDonneeClimatique.pointGeographique.eq(pointGeographique))
            .where(qDonneeClimatique.scenario.eq(scenario))
            .where(qDonneeClimatique.horizon.eq(horizon))
            .orderBy(qDonneeClimatique.aleaType.asc())
            .orderBy(qDonneeClimatique.id.asc())
            .fetch()
            .stream()
            .max(Comparator.comparingInt(dc -> dc.getEvolution().getScore()))
            .orElseThrow();
  }
}
