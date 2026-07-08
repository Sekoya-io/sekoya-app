package sekoya.back.business.processus.model.comparator;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.site.model.comparator.SiteComparator;

public class ProcessusComparator extends AbstractGenericEntityComparator<Long, Processus> {

  private static final long serialVersionUID = 1L;

  private static final ProcessusComparator INSTANCE = new ProcessusComparator();

  public static ProcessusComparator get() {
    return INSTANCE;
  }

  @Override
  protected int compareNotNullObjects(Processus left, Processus right) {
    int order =
        ComparisonChain.start()
            .compare(left.getSite(), right.getSite(), SiteComparator.get())
            .compare(
                left.getPriorite(), right.getPriorite(), Ordering.natural().nullsLast().reversed())
            .compare(left.getType(), right.getType(), Ordering.natural().nullsLast())
            .compare(left.getNom(), right.getNom(), Ordering.natural().nullsLast())
            .result();

    if (order == 0) {
      order = super.compareNotNullObjects(left, right);
    }

    return order;
  }
}
