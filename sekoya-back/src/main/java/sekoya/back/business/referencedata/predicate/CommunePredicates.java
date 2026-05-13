package sekoya.back.business.referencedata.predicate;

import java.util.List;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.model.atomic.CommuneTypeInsee;
import sekoya.back.util.binding.Bindings;

public final class CommunePredicates {

  public static SerializablePredicate2<Commune> typeInseeNotCommune() {
    return Predicates2.notNullAnd(
        Predicates2.compose(
            Predicates2.notNullAndNot(Predicates2.in(List.of(CommuneTypeInsee.COMMUNE))),
            Bindings.commune().typeInsee()));
  }

  private CommunePredicates() {}
}
