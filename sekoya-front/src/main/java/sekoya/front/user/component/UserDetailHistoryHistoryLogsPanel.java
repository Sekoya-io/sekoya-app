package sekoya.front.user.component;

import static sekoya.front.property.SekoyaFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import sekoya.back.business.history.model.atomic.HistoryLogEventType;
import sekoya.back.business.history.search.HistoryLogSort;
import sekoya.back.business.user.model.User;
import sekoya.back.util.binding.Bindings;
import sekoya.front.history.column.HistoryLogDetailColumn;
import sekoya.front.history.model.HistoryLogDataProvider;

public class UserDetailHistoryHistoryLogsPanel extends GenericPanel<User> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IPropertyService propertyService;

  public UserDetailHistoryHistoryLogsPanel(String id, final IModel<? extends User> userModel) {
    super(id, userModel);
    setOutputMarkupPlaceholderTag(true);

    HistoryLogDataProvider dataProvider =
        new HistoryLogDataProvider(
            dataModel ->
                dataModel.bind(
                    Bindings.historyLogSearchQueryData().allObjects(),
                    LoadableDetachableModel.of(
                        () ->
                            HistoryEntityReference.from(
                                GenericEntityReference.of(userModel.getObject())))));
    dataProvider
        .getDataModel()
        .getObject()
        .addMandatoryDifferencesEventType(HistoryLogEventType.UPDATE);

    add(
        DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
            .addLabelColumn(
                new ResourceModel("business.historyLog.date"), Bindings.historyLog().date())
            .withSort(HistoryLogSort.DATE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
            .withClass("cell-w-150")
            .addLabelColumn(
                new ResourceModel("business.historyLog.subject"), Bindings.historyLog().subject())
            .withClass("cell-w-250")
            .addColumn(new HistoryLogDetailColumn())
            .withClass("cell-w-500")
            .bootstrapCard()
            .title("user.detail.history.historyLogs.title")
            .ajaxPager(AddInPlacement.FOOTER_RIGHT)
            .build("history", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE)));
  }
}
