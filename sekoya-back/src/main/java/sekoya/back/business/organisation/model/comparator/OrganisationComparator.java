package sekoya.back.business.organisation.model.comparator;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import sekoya.back.business.organisation.model.Organisation;

public class OrganisationComparator extends AbstractGenericEntityComparator<Long, Organisation> {

  private static final long serialVersionUID = 1L;

  private static final OrganisationComparator INSTANCE = new OrganisationComparator();

  public static OrganisationComparator get() {
    return INSTANCE;
  }

  @Override
  protected int compareNotNullObjects(Organisation left, Organisation right) {
    int order =
        ComparisonChain.start()
            .compare(left.getNom(), right.getNom(), Ordering.natural().nullsLast())
            .result();

    if (order == 0) {
      order = super.compareNotNullObjects(left, right);
    }

    return order;
  }
}
