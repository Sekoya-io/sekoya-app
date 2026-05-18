package sekoya.back.business.organisation.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;
import sekoya.back.business.organisation.model.Organisation;

@Service
public class OrganisationSearchQueryImpl implements IOrganisationSearchQuery {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Collection<Organisation> list(
      OrganisationSearchQueryData data,
      Map<OrganisationSort, ISort.SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return Search.session(entityManager)
        .search(Organisation.class)
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchHits(offset, limit);
  }

  @Override
  public long size(OrganisationSearchQueryData data) {
    return Search.session(entityManager)
        .search(Organisation.class)
        .where(predicateContributor(data))
        .fetchTotalHitCount();
  }

  @Override
  public Collection<Long> listIds(
      OrganisationSearchQueryData data, Map<OrganisationSort, ISort.SortOrder> sorts) {
    return Search.session(entityManager)
        .search(Organisation.class)
        .select(f -> f.id(Long.class))
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchAllHits();
  }

  private BiConsumer<
          ? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(OrganisationSearchQueryData data) {
    return (f, root) -> {
      root.add(f.matchAll());
      if (data.getNom() != null) {
        root.add(
            f.simpleQueryString()
                .field(Organisation.NOM_AUTOCOMPLETE)
                .matching(wildcardTokensOr(data.getNom())));
      }
    };
  }
}
