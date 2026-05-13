package sekoya.back.business.referencedata.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

import java.util.function.BiConsumer;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import sekoya.back.business.referencedata.model.Departement;

public class DepartementSearchQueryImpl
    extends AbstractReferenceDataSearchQueryImpl<
        Departement, DepartementSort, DepartementSearchQueryData>
    implements IDepartementSearchQuery {

  public DepartementSearchQueryImpl() {
    super(Departement.class);
  }

  @Override
  protected BiConsumer<SearchPredicateFactory, SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(DepartementSearchQueryData data) {
    return super.predicateContributor(data)
        .andThen(
            (f, root) -> {
              if (data.getTerm() != null) {
                root.add(
                    f.simpleQueryString()
                        .fields(Departement.LABEL_AUTOCOMPLETE)
                        .matching(wildcardTokensOr(data.getTerm())));
              }
              if (data.getCodeInsee() != null) {
                root.add(
                    f.simpleQueryString()
                        .field(Departement.CODE_INSEE_AUTOCOMPLETE)
                        .matching(wildcardTokensOr(data.getCodeInsee())));
              }
              if (data.getRegion() != null) {
                root.add(f.match().field(Departement.REGION).matching(data.getRegion()));
              }
            });
  }
}
