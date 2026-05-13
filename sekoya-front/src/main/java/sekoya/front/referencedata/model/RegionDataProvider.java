package sekoya.front.referencedata.model;

import com.google.common.collect.ImmutableMap;
import java.util.function.UnaryOperator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import org.iglooproject.wicket.more.model.data.DataModel;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;
import sekoya.back.business.referencedata.model.Region;
import sekoya.back.business.referencedata.search.IRegionSearchQuery;
import sekoya.back.business.referencedata.search.RegionSearchQueryData;
import sekoya.back.business.referencedata.search.RegionSort;
import sekoya.back.util.binding.Bindings;

public class RegionDataProvider
    extends SearchQueryDataProvider<Region, RegionSort, RegionSearchQueryData, IRegionSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IRegionSearchQuery searchQuery;

  private final CompositeSortModel<RegionSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              RegionSort.POSITION, RegionSort.POSITION.getDefaultOrder(),
              RegionSort.LABEL, RegionSort.LABEL.getDefaultOrder()),
          ImmutableMap.of(RegionSort.ID, RegionSort.ID.getDefaultOrder()));

  public RegionDataProvider() {
    this(UnaryOperator.identity());
  }

  public RegionDataProvider(UnaryOperator<DataModel<RegionSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(RegionSearchQueryData::new)
                .bind(Bindings.regionSearchQueryData().term(), Model.of())
                .bind(Bindings.regionSearchQueryData().label(), Model.of())
                .bind(Bindings.regionSearchQueryData().codeInsee(), Model.of())
                .bind(Bindings.regionSearchQueryData().enabledFilter(), Model.of())));
  }

  public RegionDataProvider(IModel<RegionSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<RegionSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected IRegionSearchQuery searchQuery() {
    return searchQuery;
  }
}
