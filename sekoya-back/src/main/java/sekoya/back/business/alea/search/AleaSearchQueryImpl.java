package sekoya.back.business.alea.search;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.model.Alea;

@Service
public class AleaSearchQueryImpl implements IAleaSearchQuery {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Collection<Alea> list(
      AleaSearchQueryData data,
      Map<AleaSort, ISort.SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return Search.session(entityManager)
        .search(Alea.class)
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchHits(offset, limit);
  }

  @Override
  public long size(AleaSearchQueryData data) {
    return Search.session(entityManager)
        .search(Alea.class)
        .where(predicateContributor(data))
        .fetchTotalHitCount();
  }

  @Override
  public Collection<Long> listIds(AleaSearchQueryData data, Map<AleaSort, ISort.SortOrder> sorts) {
    return Search.session(entityManager)
        .search(Alea.class)
        .select(f -> f.id(Long.class))
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchAllHits();
  }

  private BiConsumer<
          ? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(AleaSearchQueryData data) {
    return (f, root) -> {
      root.add(f.matchAll());

      Objects.requireNonNull(data.getProcessus());
      root.add(f.match().field(Alea.PROCESSUS).matching(data.getProcessus()));

      if (data.getSensibilite() != null) {
        root.add(f.match().field(Alea.SENSIBILITE).matching(data.getSensibilite()));
      }
    };
  }
}
