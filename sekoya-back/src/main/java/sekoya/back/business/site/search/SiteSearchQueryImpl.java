package sekoya.back.business.site.search;

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
import sekoya.back.business.site.model.Site;

@Service
public class SiteSearchQueryImpl implements ISiteSearchQuery {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Collection<Site> list(
      SiteSearchQueryData data,
      Map<SiteSort, ISort.SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    return Search.session(entityManager)
        .search(Site.class)
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchHits(offset, limit);
  }

  @Override
  public long size(SiteSearchQueryData data) {
    return Search.session(entityManager)
        .search(Site.class)
        .where(predicateContributor(data))
        .fetchTotalHitCount();
  }

  @Override
  public Collection<Long> listIds(SiteSearchQueryData data, Map<SiteSort, ISort.SortOrder> sorts) {
    return Search.session(entityManager)
        .search(Site.class)
        .select(f -> f.id(Long.class))
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchAllHits();
  }

  private BiConsumer<
          ? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(SiteSearchQueryData data) {
    return (f, root) -> {
      root.add(f.matchAll());
      if (data.getOrganisation() != null) {
        root.add(f.match().field(Site.ORGANISATION).matching(data.getOrganisation()));
      }
      if (data.getNom() != null) {
        root.add(
            f.simpleQueryString()
                .field(Site.NOM_AUTOCOMPLETE)
                .matching(wildcardTokensOr(data.getNom())));
      }
      if (data.getTypologie() != null) {
        root.add(f.match().field(Site.TYPOLOGIE).matching(data.getTypologie()));
      }
      if (data.getCommune() != null) {
        root.add(f.match().field(Site.ADRESSE_COMMUNE).matching(data.getCommune()));
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
