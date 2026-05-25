package sekoya.back.business.alea.search;

import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;
import sekoya.back.business.alea.model.Alea;

public interface IAleaSearchQuery
    extends IHibernateSearchSearchQuery<Alea, AleaSort, AleaSearchQueryData> {

  Collection<Long> listIds(AleaSearchQueryData data, Map<AleaSort, ISort.SortOrder> sorts);
}
