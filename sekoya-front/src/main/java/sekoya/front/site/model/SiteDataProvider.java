package sekoya.front.site.model;

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
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.search.ISiteSearchQuery;
import sekoya.back.business.site.search.SiteSearchQueryData;
import sekoya.back.business.site.search.SiteSort;
import sekoya.back.util.binding.Bindings;

public class SiteDataProvider
    extends SearchQueryDataProvider<Site, SiteSort, SiteSearchQueryData, ISiteSearchQuery> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ISiteSearchQuery searchQuery;

  private final CompositeSortModel<SiteSort> sortModel =
      new CompositeSortModel<>(
          CompositingStrategy.LAST_ONLY,
          ImmutableMap.of(
              SiteSort.ADRESSE_COMMUNE_LABEL, SiteSort.ADRESSE_COMMUNE_LABEL.getDefaultOrder(),
              SiteSort.NOM, SiteSort.NOM.getDefaultOrder(),
              SiteSort.ID, SiteSort.ID.getDefaultOrder()),
          ImmutableMap.of(SiteSort.ID, SiteSort.ID.getDefaultOrder()));

  public SiteDataProvider() {
    this(UnaryOperator.identity());
  }

  public SiteDataProvider(UnaryOperator<DataModel<SiteSearchQueryData>> dataModelOperator) {
    this(
        dataModelOperator.apply(
            new DataModel<>(SiteSearchQueryData::new)
                .bind(Bindings.siteSearchQueryData().organisation(), new GenericEntityModel<>())
                .bind(Bindings.siteSearchQueryData().nom(), Model.of())
                .bind(Bindings.siteSearchQueryData().typologie(), Model.of())
                .bind(Bindings.siteSearchQueryData().commune(), new GenericEntityModel<>())
                .bind(Bindings.siteSearchQueryData().enabledFilter(), Model.of())));
  }

  public SiteDataProvider(IModel<SiteSearchQueryData> dataModel) {
    super(dataModel);
  }

  @Override
  public CompositeSortModel<SiteSort> getSortModel() {
    return sortModel;
  }

  @Override
  protected ISiteSearchQuery searchQuery() {
    return searchQuery;
  }
}
