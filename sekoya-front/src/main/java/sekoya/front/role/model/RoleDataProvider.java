package sekoya.front.role.model;

import com.google.common.collect.ImmutableMap;
import java.util.function.UnaryOperator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.search.IRoleSearchQuery;
import sekoya.back.business.role.search.RoleSearchQueryData;
import sekoya.back.business.role.search.RoleSort;
import sekoya.back.util.binding.Bindings;

public class RoleDataProvider
    extends SearchQueryDataProvider<Role, RoleSort, RoleSearchQueryData, IRoleSearchQuery> {

  private static final long serialVersionUID = -1L;

  @SpringBean private IRoleSearchQuery searchQuery;

  private final CompositeSortModel<RoleSort> sortModel =
      new CompositeSortModel<>(
          CompositeSortModel.CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              RoleSort.TITLE, RoleSort.TITLE.getDefaultOrder(),
              RoleSort.ID, RoleSort.ID.getDefaultOrder()),
          ImmutableMap.of(RoleSort.ID, RoleSort.ID.getDefaultOrder()));

  public RoleDataProvider() {
    this(UnaryOperator.identity());
  }

  public RoleDataProvider(UnaryOperator<DataModel<RoleSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(RoleSearchQueryData::new)
                .bind(Bindings.roleSearchQueryData().user(), new GenericEntityModel<>())));
  }

  public RoleDataProvider(IModel<RoleSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<RoleSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected IRoleSearchQuery searchQuery() {
    return searchQuery;
  }
}
