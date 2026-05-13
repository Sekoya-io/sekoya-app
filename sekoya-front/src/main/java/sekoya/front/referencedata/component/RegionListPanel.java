package sekoya.front.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;
import sekoya.back.business.referencedata.model.Region;
import sekoya.back.business.referencedata.search.RegionSearchQueryData;
import sekoya.back.business.referencedata.search.RegionSort;
import sekoya.back.util.binding.Bindings;
import sekoya.front.referencedata.model.RegionDataProvider;

public class RegionListPanel
    extends AbstractReferenceDataSimpleListPanel<
        Region, RegionSort, RegionSearchQueryData, RegionDataProvider> {

  private static final long serialVersionUID = 1L;

  public RegionListPanel(String id) {
    super(id, new RegionDataProvider());
  }

  @Override
  protected IColumnState<Region, RegionSort> addColumns(
      DataTableBuilder<Region, RegionSort> builder) {
    return super.addColumns(builder)
        .addLabelColumn(
            new ResourceModel("business.referenceData.label"), Bindings.region().label())
        .withSort(RegionSort.LABEL, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-250")
        .addLabelColumn(
            new ResourceModel("business.departement.codeInsee.short"),
            Bindings.region().codeInsee())
        .withSort(RegionSort.CODE_INSEE, SortIconStyle.DEFAULT, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-100");
  }

  @Override
  protected Component createSearchForm(
      String wicketId,
      RegionDataProvider dataProvider,
      DecoratedCoreDataTablePanel<Region, RegionSort> table) {
    dataProvider.getDataModel().getObject().setEnabledFilter(EnabledFilter.ENABLED_ONLY);
    return new RegionSearchPanel(wicketId, dataProvider, table);
  }
}
