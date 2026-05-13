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
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.search.CommuneSearchQueryData;
import sekoya.back.business.referencedata.search.CommuneSort;
import sekoya.back.business.referencedata.search.ICommuneSearchQuery;
import sekoya.back.util.binding.Bindings;

public class CommuneDataProvider
    extends SearchQueryDataProvider<
        Commune, CommuneSort, CommuneSearchQueryData, ICommuneSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ICommuneSearchQuery searchQuery;

  private final CompositeSortModel<CommuneSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              CommuneSort.POSITION, CommuneSort.POSITION.getDefaultOrder(),
              CommuneSort.LABEL, CommuneSort.LABEL.getDefaultOrder()),
          ImmutableMap.of(CommuneSort.ID, CommuneSort.ID.getDefaultOrder()));

  public CommuneDataProvider() {
    this(UnaryOperator.identity());
  }

  public CommuneDataProvider(UnaryOperator<DataModel<CommuneSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(CommuneSearchQueryData::new)
                .bind(Bindings.communeSearchQueryData().term(), Model.of())
                .bind(Bindings.communeSearchQueryData().label(), Model.of())
                .bind(Bindings.communeSearchQueryData().codePostal(), Model.of())
                .bind(Bindings.communeSearchQueryData().codeInsee(), Model.of())
                .bind(Bindings.communeSearchQueryData().typeInsee(), Model.of())
                .bind(Bindings.communeSearchQueryData().departement(), new GenericEntityModel<>())
                .bind(Bindings.communeSearchQueryData().region(), new GenericEntityModel<>())
                .bind(Bindings.communeSearchQueryData().enabledFilter(), Model.of())));
  }

  public CommuneDataProvider(IModel<CommuneSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<CommuneSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected ICommuneSearchQuery searchQuery() {
    return searchQuery;
  }
}
