package sekoya.back.business.processus.predicate;

import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.util.binding.Bindings;

public final class ProcessusPredicates {

  public static SerializablePredicate2<Processus> enabled() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.processus().enabled()));
  }

  public static SerializablePredicate2<Processus> disabled() {
    return Predicates2.notNullAndNot(enabled());
  }

  private ProcessusPredicates() {}
}
