package sekoya.back.business.user.predicate;

import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.util.binding.Bindings;

public final class UserPredicates {

  private static SerializablePredicate2<User> type(UserType type) {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.equalTo(type), Bindings.user().type()));
  }

  public static SerializablePredicate2<User> administrateurTechnique() {
    return type(UserType.ADMINISTRATEUR_TECHNIQUE);
  }

  public static SerializablePredicate2<User> administrateurFonctionnel() {
    return type(UserType.ADMINISTRATEUR_FONCTIONNEL);
  }

  public static SerializablePredicate2<User> organisation() {
    return type(UserType.ORGANISATION);
  }

  public static SerializablePredicate2<User> enabled() {
    return Predicates2.notNullAnd(
        Predicates2.compose(Predicates2.isTrue(), Bindings.user().enabled()));
  }

  public static SerializablePredicate2<User> disabled() {
    return Predicates2.notNullAndNot(enabled());
  }

  public static SerializablePredicate2<User> announcementOpen() {
    return Predicates2.notNullAnd(
        Predicates2.compose(
            Predicates2.isTrue(), Bindings.user().announcementInformation().open()));
  }

  public static SerializablePredicate2<User> announcementClose() {
    return Predicates2.notNullAndNot(announcementOpen());
  }

  private UserPredicates() {}
}
