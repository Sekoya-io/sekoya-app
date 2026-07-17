package sekoya.front.site.component;

import static sekoya.front.common.util.CssClassConstants.BTN_TABLE_ROW_ACTION;
import static sekoya.front.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.search.ProcessusSort;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.SekoyaSession;
import sekoya.front.common.component.ScoreRatingDisplayPanel;
import sekoya.front.common.renderer.ActionRenderers;
import sekoya.front.common.util.CssClassConstants;
import sekoya.front.processus.model.ProcessusDataProvider;
import sekoya.front.processus.page.ProcessusDetailPage;
import sekoya.front.processus.page.ProcessusEditPage;
import sekoya.front.processus.page.ProcessusSiteAddPage;

public class SiteDetailProcessusPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IPropertyService propertyService;

  public SiteDetailProcessusPanel(String id, IModel<Site> siteModel) {
    super(id, siteModel);

    ProcessusDataProvider dataProvider = new ProcessusDataProvider();
    dataProvider
        .getDataModel()
        .getObject()
        .setOrganisation(SekoyaSession.get().getOrganisationModel().getObject());
    dataProvider.getDataModel().getObject().setSite(siteModel.getObject());

    DecoratedCoreDataTablePanel<Processus, ?> results =
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addColumn(
                new AbstractCoreColumn<Processus, ProcessusSort>(
                    new ResourceModel("business.processus.nom")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Processus>> cellItem,
                      String componentId,
                      IModel<Processus> rowModel) {
                    cellItem.add(new NomCellFragment(componentId, rowModel));
                  }
                })
            .withSort(ProcessusSort.NOM, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-250")
            .addLabelColumn(
                new ResourceModel("business.processus.thematique"),
                Bindings.processus().thematique())
            .withSort(ProcessusSort.THEMATIQUE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-200")
            .addColumn(
                new AbstractCoreColumn<Processus, ProcessusSort>(
                    new ResourceModel("business.processus.priorite")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public void populateItem(
                      Item<ICellPopulator<Processus>> cellItem,
                      String componentId,
                      IModel<Processus> rowModel) {
                    cellItem.add(
                        new ScoreRatingDisplayPanel<>(
                                componentId,
                                BindingModel.of(rowModel, Bindings.processus().priorite()))
                            .small());
                  }
                })
            .withSort(ProcessusSort.PRIORITE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-200")
            .addLabelColumn(
                new ResourceModel("business.processus.aleas"), Bindings.processus().aleas().size())
            .withClass("cell-w-100")
            .withClass(CssClassConstants.CELL_DISPLAY_XL)
            .addActionColumn()
            .addLink(ActionRenderers.edit(), ProcessusEditPage.MAPPER)
            .hideIfInvalid()
            .withClassOnElements(BTN_TABLE_ROW_ACTION)
            .end()
            .withClass("cell-w-actions-1x cell-w-fit")
            .rows()
            .withClass(
                itemModel ->
                    Condition.predicate(
                            itemModel,
                            Predicates2.compose(
                                Predicates2.isFalse(), Bindings.processus().enabled()))
                        .then(TABLE_ROW_DISABLED)
                        .otherwise(""))
            .end()
            .bootstrapCard()
            .addIn(
                AddInPlacement.HEADING_MAIN,
                (wicketId, table) ->
                    new SiteDetailProcessusSearchPanel(wicketId, dataProvider, table))
            .addIn(
                AddInPlacement.HEADING_RIGHT,
                (wicketId, _) -> new ActionsGlobalesFragment(wicketId, siteModel))
            .ajaxPagers()
            .count("processus.common.count")
            .build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));

    add(results);
  }

  private class ActionsGlobalesFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public ActionsGlobalesFragment(String id, IModel<Site> siteModel) {
      super(id, "actionsGlobalesFragment", SiteDetailProcessusPanel.this, siteModel);

      add(Condition.anyChildVisible(this).thenShow());

      add(ProcessusSiteAddPage.MAPPER.map(siteModel).link("add").hideIfInvalid());
    }
  }

  private class NomCellFragment extends Fragment {
    private static final long serialVersionUID = 1L;

    public NomCellFragment(String id, IModel<Processus> processusModel) {
      super(id, "nomCellFragment", SiteDetailProcessusPanel.this);

      add(
          ProcessusDetailPage.MAPPER
              .map(processusModel)
              .link("processusLink")
              .add(new CoreLabel("nom", BindingModel.of(processusModel, Bindings.processus()))),
          new CoreLabel("type", BindingModel.of(processusModel, Bindings.processus().type())));
    }
  }
}
