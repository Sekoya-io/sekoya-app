package sekoya.back.business.user.model.comparator;

import com.google.common.collect.ComparisonChain;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import sekoya.back.business.user.model.UserOrganisation;

public class UserOrganisationComparator
    extends AbstractGenericEntityComparator<Long, UserOrganisation> {

  private static final long serialVersionUID = 1L;

  private static final UserOrganisationComparator INSTANCE = new UserOrganisationComparator();

  public static UserOrganisationComparator get() {
    return INSTANCE;
  }

  @Override
  protected int compareNotNullObjects(UserOrganisation left, UserOrganisation right) {
    int order =
        ComparisonChain.start()
            .compare(left.getUser(), right.getUser(), UserComparator.get())
            .result();

    if (order == 0) {
      order = super.compareNotNullObjects(left, right);
    }

    return order;
  }
}
