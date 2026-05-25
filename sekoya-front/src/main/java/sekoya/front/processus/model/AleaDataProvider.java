package sekoya.front.processus.model;

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
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.search.AleaSearchQueryData;
import sekoya.back.business.alea.search.AleaSort;
import sekoya.back.business.alea.search.IAleaSearchQuery;
import sekoya.back.util.binding.Bindings;

public class AleaDataProvider
    extends SearchQueryDataProvider<Alea, AleaSort, AleaSearchQueryData, IAleaSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IAleaSearchQuery searchQuery;

  private final CompositeSortModel<AleaSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              AleaSort.IMPACT_POTENTIEL_BRUT, AleaSort.IMPACT_POTENTIEL_BRUT.getDefaultOrder(),
              AleaSort.TYPE, AleaSort.TYPE.getDefaultOrder(),
              AleaSort.ID, AleaSort.ID.getDefaultOrder()),
          ImmutableMap.of(AleaSort.ID, AleaSort.ID.getDefaultOrder()));

  public AleaDataProvider() {
    this(UnaryOperator.identity());
  }

  public AleaDataProvider(UnaryOperator<DataModel<AleaSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(AleaSearchQueryData::new)
                .bind(Bindings.aleaSearchQueryData().processus(), new GenericEntityModel<>())
                .bind(Bindings.aleaSearchQueryData().sensibilite(), Model.of())));
  }

  public AleaDataProvider(IModel<AleaSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<AleaSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected IAleaSearchQuery searchQuery() {
    return searchQuery;
  }
}
