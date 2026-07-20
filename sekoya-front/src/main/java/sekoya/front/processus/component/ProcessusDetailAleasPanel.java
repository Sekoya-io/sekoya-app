package sekoya.front.processus.component;

import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.search.AleaSort;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.component.HistoryEventSummaryPanel;
import sekoya.front.common.component.ScoreRatingDisplayPanel;
import sekoya.front.processus.model.AleaDataProvider;

public class ProcessusDetailAleasPanel extends GenericPanel<Processus> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IPropertyService propertyService;

  public ProcessusDetailAleasPanel(String id, IModel<Processus> processusModel) {
    super(id, processusModel);

    AleaDataProvider dataProvider = new AleaDataProvider();
    dataProvider.getDataModel().getObject().setProcessus(processusModel.getObject());

    DecoratedCoreDataTablePanel<Alea, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addColumn(
                new AbstractCoreColumn<Alea, AleaSort>(new ResourceModel("business.alea.type")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Alea>> cellItem,
                      String componentId,
                      IModel<Alea> rowModel) {
                    cellItem.add(new TypeCellFragment(componentId, rowModel));
                  }
                })
            .withSort(AleaSort.TYPE, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addColumn(
                new AbstractCoreColumn<Alea, AleaSort>(
                    new ResourceModel("business.alea.sensibilite")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Alea>> cellItem,
                      String componentId,
                      IModel<Alea> rowModel) {
                    cellItem.add(new SensibiliteCellFragment(componentId, rowModel));
                  }
                })
            .withClass("cell-w-250")
            .addColumn(
                new AbstractCoreColumn<Alea, AleaSort>(
                    new ResourceModel("business.alea.impactPotentielBrut")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Alea>> cellItem,
                      String componentId,
                      IModel<Alea> rowModel) {
                    cellItem.add(new ImpactPotentielBrutCellFragment(componentId, rowModel));
                  }
                })
            .withSort(
                AleaSort.IMPACT_POTENTIEL_BRUT, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addColumn(
                new AbstractCoreColumn<Alea, AleaSort>(Model.of()) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Alea>> cellItem,
                      String componentId,
                      IModel<Alea> rowModel) {
                    cellItem.add(
                        new HistoryEventSummaryPanel(
                            componentId,
                            BindingModel.of(rowModel, Bindings.alea().creation()),
                            BindingModel.of(rowModel, Bindings.alea().modification())));
                  }
                })
            .withClass("cell-w-80 cell-w-fit text-center")
            .bootstrapCard()
            .addIn(
                AddInPlacement.HEADING_MAIN,
                (wicketId, table) ->
                    new ProcessusDetailAleasSearchPanel(wicketId, dataProvider, table))
            .ajaxPagers()
            .count("alea.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(results);
  }

  private class TypeCellFragment extends Fragment {
    private static final long serialVersionUID = 1L;

    public TypeCellFragment(String id, IModel<Alea> aleaModel) {
      super(id, "typeCellFragment", ProcessusDetailAleasPanel.this);

      add(
          new WebMarkupContainer("icon")
              .add(
                  new ClassAttributeAppender(
                      BindingModel.of(aleaModel, Bindings.alea().type().iconCssClass()))),
          new CoreLabel("type", BindingModel.of(aleaModel, Bindings.alea().type())));
    }
  }

  private class SensibiliteCellFragment extends Fragment {
    private static final long serialVersionUID = 1L;

    public SensibiliteCellFragment(String id, IModel<Alea> aleaModel) {
      super(id, "sensibiliteCellFragment", ProcessusDetailAleasPanel.this);

      add(
          new ScoreRatingDisplayPanel<>(
                  "sensibilite", BindingModel.of(aleaModel, Bindings.alea().sensibilite()))
              .small());
    }
  }

  private class ImpactPotentielBrutCellFragment extends Fragment {
    private static final long serialVersionUID = 1L;

    public ImpactPotentielBrutCellFragment(String id, IModel<Alea> aleaModel) {
      super(id, "impactPotentielBrutCellFragment", ProcessusDetailAleasPanel.this);

      add(
          new ScoreRatingDisplayPanel<>(
                  "impactPotentielBrut",
                  BindingModel.of(aleaModel, Bindings.alea().impactPotentielBrut()))
              .small());
    }
  }
}
