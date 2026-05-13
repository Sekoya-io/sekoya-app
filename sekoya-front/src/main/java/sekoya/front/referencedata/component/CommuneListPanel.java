package sekoya.front.referencedata.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.model.BindingModel;
import igloo.wicket.renderer.Renderer;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.functional.Joiners;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.predicate.CommunePredicates;
import sekoya.back.business.referencedata.search.CommuneSearchQueryData;
import sekoya.back.business.referencedata.search.CommuneSort;
import sekoya.back.util.binding.Bindings;
import sekoya.front.referencedata.model.CommuneDataProvider;

public class CommuneListPanel
    extends AbstractReferenceDataSimpleListPanel<
        Commune, CommuneSort, CommuneSearchQueryData, CommuneDataProvider> {

  private static final long serialVersionUID = 1L;

  public CommuneListPanel(String id) {
    super(id, new CommuneDataProvider());
  }

  @Override
  protected IColumnState<Commune, CommuneSort> addColumns(
      DataTableBuilder<Commune, CommuneSort> builder) {
    return super.addColumns(builder)
        .addLabelColumn(
            new ResourceModel("business.referenceData.label"), Bindings.commune().label())
        .withSort(CommuneSort.LABEL, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-200")
        .addLabelColumn(
            new ResourceModel("business.commune.codesPostaux.short"),
            Bindings.commune().codesPostaux(),
            Renderer.fromJoiner(Joiners.Functions.onNewLine()))
        .withSort(CommuneSort.CODES_POSTAUX, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .multiline()
        .withClass("cell-w-100")
        .addColumn(
            new AbstractCoreColumn<Commune, CommuneSort>(
                new ResourceModel("business.commune.codeInsee.short")) {
              private static final long serialVersionUID = 1L;

              @Override
              public void populateItem(
                  Item<ICellPopulator<Commune>> cellItem,
                  String componentId,
                  IModel<Commune> rowModel) {
                cellItem.add(new CodeInseeCellFragment(componentId, rowModel));
              }
            })
        .withSort(CommuneSort.CODE_INSEE, SortIconStyle.DEFAULT, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-120")
        .addLabelColumn(
            new ResourceModel("business.commune.departement"), Bindings.commune().departement())
        .withSort(CommuneSort.DEPARTEMENT, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-150")
        .addLabelColumn(
            new ResourceModel("business.departement.region"),
            Bindings.commune().departement().region())
        .withSort(CommuneSort.REGION, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
        .withClass("cell-w-200");
  }

  @Override
  protected Component createSearchForm(
      String wicketId,
      CommuneDataProvider dataProvider,
      DecoratedCoreDataTablePanel<Commune, CommuneSort> table) {
    dataProvider.getDataModel().getObject().setEnabledFilter(EnabledFilter.ENABLED_ONLY);
    return new CommuneSearchPanel(wicketId, dataProvider, table);
  }

  private class CodeInseeCellFragment extends Fragment {
    private static final long serialVersionUID = 1L;

    public CodeInseeCellFragment(String id, IModel<Commune> communeModel) {
      super(id, "codeInseeCellFragment", CommuneListPanel.this);

      add(
          new CoreLabel("codeInsee", BindingModel.of(communeModel, Bindings.commune().codeInsee())),
          new CoreLabel("typeInsee", BindingModel.of(communeModel, Bindings.commune().typeInsee()))
              .add(
                  Condition.predicate(communeModel, CommunePredicates.typeInseeNotCommune())
                      .thenShow()));
    }
  }
}
