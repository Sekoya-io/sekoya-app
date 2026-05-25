package sekoya.front.processus.component;

import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.wicketstuff.wiquery.core.events.StateEvent;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaSensibilite;
import sekoya.back.util.binding.Bindings;
import sekoya.front.processus.model.AleaDataProvider;

public class ProcessusDetailAleaListSearchPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public ProcessusDetailAleaListSearchPanel(
      String id, AleaDataProvider dataProvider, DecoratedCoreDataTablePanel<Alea, ?> table) {
    super(id);

    PageableSearchForm<Void> form = new PageableSearchForm<>("form", table);
    add(form);

    form.add(
        new AjaxFormSubmitBehavior(form, StateEvent.CHANGE.getEventLabel()) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            // Just in case the dataProvider's content was loaded before search parameters changed
            dataProvider.detach();
            target.add(table);
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        });

    form.add(
        new EnumDropDownSingleChoice<>(
                "sensibilite",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.aleaSearchQueryData().sensibilite()),
                AleaSensibilite.class)
            .setLabel(new ResourceModel("business.alea.sensibilite"))
            .add(new LabelPlaceholderBehavior()));
  }
}
