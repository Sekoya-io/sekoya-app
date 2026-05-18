package sekoya.front.organisation.model;

import com.google.common.collect.ImmutableMap;
import java.util.function.UnaryOperator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.organisation.search.IOrganisationSearchQuery;
import sekoya.back.business.organisation.search.OrganisationSearchQueryData;
import sekoya.back.business.organisation.search.OrganisationSort;
import sekoya.back.util.binding.Bindings;

public class OrganisationDataProvider
    extends SearchQueryDataProvider<
        Organisation, OrganisationSort, OrganisationSearchQueryData, IOrganisationSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IOrganisationSearchQuery searchQuery;

  private final CompositeSortModel<OrganisationSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              OrganisationSort.NOM, OrganisationSort.NOM.getDefaultOrder(),
              OrganisationSort.ID, OrganisationSort.ID.getDefaultOrder()),
          ImmutableMap.of(OrganisationSort.ID, OrganisationSort.ID.getDefaultOrder()));

  public OrganisationDataProvider() {
    this(UnaryOperator.identity());
  }

  public OrganisationDataProvider(
      UnaryOperator<DataModel<OrganisationSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(OrganisationSearchQueryData::new)
                .bind(Bindings.organisationSearchQueryData().nom(), Model.of())));
  }

  public OrganisationDataProvider(IModel<OrganisationSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<OrganisationSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected IOrganisationSearchQuery searchQuery() {
    return searchQuery;
  }
}
