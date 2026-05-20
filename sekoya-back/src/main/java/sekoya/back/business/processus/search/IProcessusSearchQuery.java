package sekoya.back.business.processus.search;

import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;
import sekoya.back.business.processus.model.Processus;

public interface IProcessusSearchQuery
    extends IHibernateSearchSearchQuery<Processus, ProcessusSort, ProcessusSearchQueryData> {

  Collection<Long> listIds(
      ProcessusSearchQueryData data, Map<ProcessusSort, ISort.SortOrder> sorts);
}
