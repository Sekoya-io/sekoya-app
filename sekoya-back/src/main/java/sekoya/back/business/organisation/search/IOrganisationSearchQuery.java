package sekoya.back.business.organisation.search;

import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;
import sekoya.back.business.organisation.model.Organisation;

public interface IOrganisationSearchQuery
    extends IHibernateSearchSearchQuery<
        Organisation, OrganisationSort, OrganisationSearchQueryData> {

  Collection<Long> listIds(
      OrganisationSearchQueryData data, Map<OrganisationSort, ISort.SortOrder> sorts);
}
