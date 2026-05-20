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
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.search.IProcessusSearchQuery;
import sekoya.back.business.processus.search.ProcessusSearchQueryData;
import sekoya.back.business.processus.search.ProcessusSort;
import sekoya.back.util.binding.Bindings;

public class ProcessusDataProvider
    extends SearchQueryDataProvider<
        Processus, ProcessusSort, ProcessusSearchQueryData, IProcessusSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IProcessusSearchQuery searchQuery;

  private final CompositeSortModel<ProcessusSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              ProcessusSort.PRIORITE, ProcessusSort.PRIORITE.getDefaultOrder(),
              ProcessusSort.THEMATIQUE, ProcessusSort.THEMATIQUE.getDefaultOrder(),
              ProcessusSort.NOM, ProcessusSort.NOM.getDefaultOrder(),
              ProcessusSort.ID, ProcessusSort.ID.getDefaultOrder()),
          ImmutableMap.of(ProcessusSort.ID, ProcessusSort.ID.getDefaultOrder()));

  public ProcessusDataProvider() {
    this(UnaryOperator.identity());
  }

  public ProcessusDataProvider(
      UnaryOperator<DataModel<ProcessusSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(ProcessusSearchQueryData::new)
                .bind(
                    Bindings.processusSearchQueryData().organisation(), new GenericEntityModel<>())
                .bind(Bindings.processusSearchQueryData().site(), new GenericEntityModel<>())
                .bind(Bindings.processusSearchQueryData().thematique(), Model.of())
                .bind(Bindings.processusSearchQueryData().nom(), Model.of())
                .bind(Bindings.processusSearchQueryData().priorite(), Model.of())
                .bind(Bindings.processusSearchQueryData().enabledFilter(), Model.of())));
  }

  public ProcessusDataProvider(IModel<ProcessusSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<ProcessusSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected IProcessusSearchQuery searchQuery() {
    return searchQuery;
  }
}
