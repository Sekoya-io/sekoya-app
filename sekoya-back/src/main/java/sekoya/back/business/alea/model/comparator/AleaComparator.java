package sekoya.back.business.alea.model.comparator;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import org.iglooproject.jpa.business.generic.util.AbstractGenericEntityComparator;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.processus.model.comparator.ProcessusComparator;

public class AleaComparator extends AbstractGenericEntityComparator<Long, Alea> {

  private static final long serialVersionUID = 1L;

  private static final AleaComparator INSTANCE = new AleaComparator();

  public static AleaComparator get() {
    return INSTANCE;
  }

  @Override
  protected int compareNotNullObjects(Alea left, Alea right) {
    int order =
        ComparisonChain.start()
            .compare(left.getProcessus(), right.getProcessus(), ProcessusComparator.get())
            .compare(left.getType(), right.getType(), Ordering.natural().nullsLast())
            .result();

    if (order == 0) {
      order = super.compareNotNullObjects(left, right);
    }

    return order;
  }
}
