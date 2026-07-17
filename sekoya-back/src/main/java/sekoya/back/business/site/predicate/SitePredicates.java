package sekoya.back.business.site.predicate;

import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;

public final class SitePredicates {

  public static SerializablePredicate2<Site> enabled() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.site().enabled()));
  }

  public static SerializablePredicate2<Site> disabled() {
    return Predicates2.notNullAndNot(enabled());
  }

  private SitePredicates() {}
}
