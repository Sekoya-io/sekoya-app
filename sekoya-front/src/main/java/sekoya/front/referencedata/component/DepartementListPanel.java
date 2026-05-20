package sekoya.front.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;
import sekoya.back.business.referencedata.model.Departement;
import sekoya.back.business.referencedata.search.DepartementSearchQueryData;
import sekoya.back.business.referencedata.search.DepartementSort;
import sekoya.back.util.binding.Bindings;
import sekoya.front.referencedata.model.DepartementDataProvider;

public class DepartementListPanel
    extends AbstractReferenceDataSimpleListPanel<
        Departement, DepartementSort, DepartementSearchQueryData, DepartementDataProvider> {

  private static final long serialVersionUID = 1L;

  public DepartementListPanel(String id) {
    super(id, new DepartementDataProvider());
  }

  @Override
  protected IColumnState<Departement, DepartementSort> addColumns(
      DataTableBuilder<Departement, DepartementSort> builder) {
    return super.addColumns(builder)
        .addLabelColumn(
            new ResourceModel("business.referenceData.label"), Bindings.departement().label())
        .withSort(DepartementSort.LABEL, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-200")
        .addLabelColumn(
            new ResourceModel("business.departement.codeInsee.short"),
            Bindings.departement().codeInsee())
        .withSort(DepartementSort.CODE_INSEE, SortIconStyle.DEFAULT, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-100")
        .addLabelColumn(
            new ResourceModel("business.departement.region"), Bindings.departement().region())
        .withSort(
            DepartementSort.REGION_LABEL, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-250");
  }

  @Override
  protected Component createSearchForm(
      String wicketId,
      DepartementDataProvider dataProvider,
      DecoratedCoreDataTablePanel<Departement, DepartementSort> table) {
    dataProvider.getDataModel().getObject().setEnabledFilter(EnabledFilter.ENABLED_ONLY);
    return new DepartementListSearchPanel(wicketId, dataProvider, table);
  }
}
