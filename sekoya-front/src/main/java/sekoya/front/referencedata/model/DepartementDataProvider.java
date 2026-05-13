package sekoya.front.referencedata.model;

import com.google.common.collect.ImmutableMap;
import java.util.function.UnaryOperator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;
import sekoya.back.business.referencedata.model.Departement;
import sekoya.back.business.referencedata.search.DepartementSearchQueryData;
import sekoya.back.business.referencedata.search.DepartementSort;
import sekoya.back.business.referencedata.search.IDepartementSearchQuery;
import sekoya.back.util.binding.Bindings;

public class DepartementDataProvider
    extends SearchQueryDataProvider<
        Departement, DepartementSort, DepartementSearchQueryData, IDepartementSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IDepartementSearchQuery searchQuery;

  private final CompositeSortModel<DepartementSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              DepartementSort.POSITION, DepartementSort.POSITION.getDefaultOrder(),
              DepartementSort.LABEL, DepartementSort.LABEL.getDefaultOrder()),
          ImmutableMap.of(DepartementSort.ID, DepartementSort.ID.getDefaultOrder()));

  public DepartementDataProvider() {
    this(UnaryOperator.identity());
  }

  public DepartementDataProvider(
      UnaryOperator<DataModel<DepartementSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(DepartementSearchQueryData::new)
                .bind(Bindings.departementSearchQueryData().term(), Model.of())
                .bind(Bindings.departementSearchQueryData().label(), Model.of())
                .bind(Bindings.departementSearchQueryData().codeInsee(), Model.of())
                .bind(Bindings.departementSearchQueryData().region(), new GenericEntityModel<>())
                .bind(Bindings.departementSearchQueryData().enabledFilter(), Model.of())));
  }

  public DepartementDataProvider(IModel<DepartementSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<DepartementSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected IDepartementSearchQuery searchQuery() {
    return searchQuery;
  }
}
