package sekoya.back.business.referencedata.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

import java.util.function.BiConsumer;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import sekoya.back.business.referencedata.model.Commune;

public class CommuneSearchQueryImpl
    extends AbstractReferenceDataSearchQueryImpl<Commune, CommuneSort, CommuneSearchQueryData>
    implements ICommuneSearchQuery {

  public CommuneSearchQueryImpl() {
    super(Commune.class);
  }

  @Override
  protected BiConsumer<SearchPredicateFactory, SimpleBooleanPredicateClausesCollector<?, ?>>
      predicateContributor(CommuneSearchQueryData data) {
    return super.predicateContributor(data)
        .andThen(
            (f, root) -> {
              if (data.getTerm() != null) {
                root.add(
                    f.simpleQueryString()
                        .fields(Commune.LABEL_AUTOCOMPLETE, Commune.CODES_POSTAUX_AUTOCOMPLETE)
                        .matching(wildcardTokensOr(data.getTerm())));
              }
              if (data.getCodePostal() != null) {
                root.add(
                    f.simpleQueryString()
                        .field(Commune.CODES_POSTAUX_AUTOCOMPLETE)
                        .matching(wildcardTokensOr(data.getCodePostal())));
              }
              if (data.getCodeInsee() != null) {
                root.add(
                    f.simpleQueryString()
                        .field(Commune.CODE_INSEE_AUTOCOMPLETE)
                        .matching(wildcardTokensOr(data.getCodeInsee())));
              }
              if (data.getTypeInsee() != null) {
                root.add(f.match().field(Commune.TYPE_INSEE).matching(data.getTypeInsee()));
              }
              if (data.getDepartement() != null) {
                root.add(f.match().field(Commune.DEPARTEMENT).matching(data.getDepartement()));
              }
              if (data.getRegion() != null) {
                root.add(f.match().field(Commune.DEPARTEMENT_REGION).matching(data.getRegion()));
              }
            });
  }
}
