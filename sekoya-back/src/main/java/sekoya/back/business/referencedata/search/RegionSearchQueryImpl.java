package sekoya.back.business.referencedata.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

import java.util.function.BiConsumer;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import sekoya.back.business.referencedata.model.Region;

public class RegionSearchQueryImpl
    extends AbstractReferenceDataSearchQueryImpl<Region, RegionSort, RegionSearchQueryData>
    implements IRegionSearchQuery {

  public RegionSearchQueryImpl() {
    super(Region.class);
  }

  @Override
  protected BiConsumer<SearchPredicateFactory, SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(RegionSearchQueryData data) {
    return super.predicateContributor(data)
        .andThen(
            (f, root) -> {
              if (data.getTerm() != null) {
                root.add(
                    f.simpleQueryString()
                        .fields(Region.LABEL_AUTOCOMPLETE)
                        .matching(wildcardTokensOr(data.getTerm())));
              }
              if (data.getCodeInsee() != null) {
                root.add(
                    f.simpleQueryString()
                        .field(Region.CODE_INSEE_AUTOCOMPLETE)
                        .matching(wildcardTokensOr(data.getCodeInsee())));
              }
            });
  }
}
