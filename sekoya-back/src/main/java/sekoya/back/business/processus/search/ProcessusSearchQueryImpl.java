package sekoya.back.business.processus.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

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
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.site.model.Site;

@Service
public class ProcessusSearchQueryImpl implements IProcessusSearchQuery {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Collection<Processus> list(
      ProcessusSearchQueryData data,
      Map<ProcessusSort, ISort.SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return Search.session(entityManager)
        .search(Processus.class)
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchHits(offset, limit);
  }

  @Override
  public long size(ProcessusSearchQueryData data) {
    return Search.session(entityManager)
        .search(Processus.class)
        .where(predicateContributor(data))
        .fetchTotalHitCount();
  }

  @Override
  public Collection<Long> listIds(
      ProcessusSearchQueryData data, Map<ProcessusSort, ISort.SortOrder> sorts) {
    return Search.session(entityManager)
        .search(Processus.class)
        .select(f -> f.id(Long.class))
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchAllHits();
  }

  private BiConsumer<
          ? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(ProcessusSearchQueryData data) {
    return (f, root) -> {
      root.add(f.matchAll());

      Objects.requireNonNull(data.getOrganisation());
      root.add(f.match().field(Processus.SITE_ORGANISATION).matching(data.getOrganisation()));

      if (data.getSite() != null) {
        root.add(f.match().field(Processus.SITE).matching(data.getOrganisation()));
      }
      if (data.getThematique() != null) {
        root.add(f.match().field(Processus.THEMATIQUE).matching(data.getThematique()));
      }
      if (data.getNom() != null) {
        root.add(
            f.simpleQueryString()
                .field(Site.NOM_AUTOCOMPLETE)
                .matching(wildcardTokensOr(data.getNom())));
      }
      if (data.getPriorite() != null) {
        root.add(f.match().field(Processus.PRIORITE).matching(data.getPriorite()));
      }
      if (data.getEnabledFilter() != null
          && !Objects.equals(data.getEnabledFilter(), EnabledFilter.ALL)) {
        boolean enabled =
            switch (data.getEnabledFilter()) {
              case DISABLED_ONLY -> false;
              case ENABLED_ONLY -> true;
              default -> throw new IllegalSwitchValueException(data.getEnabledFilter());
            };
        root.add(f.match().field(Site.ENABLED).matching(enabled));
      }
    };
  }
}
