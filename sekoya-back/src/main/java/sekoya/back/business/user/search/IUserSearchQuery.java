package sekoya.back.business.user.search;

import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;
import sekoya.back.business.user.model.User;

public interface IUserSearchQuery
    extends IHibernateSearchSearchQuery<User, UserSort, UserSearchQueryData> {

  Collection<Long> listIds(UserSearchQueryData data, Map<UserSort, ISort.SortOrder> sorts);
}
