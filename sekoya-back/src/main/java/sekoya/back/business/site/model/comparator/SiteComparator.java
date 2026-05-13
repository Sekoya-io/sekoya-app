package sekoya.back.business.site.model.comparator;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import sekoya.back.business.organisation.model.comparator.OrganisationComparator;
import sekoya.back.business.site.model.Site;

public class SiteComparator extends AbstractGenericEntityComparator<Long, Site> {

  private static final long serialVersionUID = 1L;

  private static final SiteComparator INSTANCE = new SiteComparator();

  public static SiteComparator get() {
    return INSTANCE;
  }

  @Override
  protected int compareNotNullObjects(Site left, Site right) {
    int order =
        ComparisonChain.start()
            .compare(left.getOrganisation(), right.getOrganisation(), OrganisationComparator.get())
            .compare(left.getNom(), right.getNom(), Ordering.natural().nullsLast())
            .result();

    if (order == 0) {
      order = super.compareNotNullObjects(left, right);
    }

    return order;
  }
}
