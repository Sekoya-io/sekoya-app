package sekoya.back.business.site.search;

import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;
import sekoya.back.business.site.model.Site;

public interface ISiteSearchQuery
    extends IHibernateSearchSearchQuery<Site, SiteSort, SiteSearchQueryData> {

  Collection<Long> listIds(SiteSearchQueryData data, Map<SiteSort, ISort.SortOrder> sorts);
}
