package sekoya.front.site.component;

import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.wicketstuff.wiquery.core.events.StateEvent;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusPriorite;
import sekoya.back.business.processus.model.atomic.ProcessusThematique;
import sekoya.back.util.binding.Bindings;
import sekoya.front.processus.model.ProcessusDataProvider;

public class SiteDetailProcessusSearchPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public SiteDetailProcessusSearchPanel(
      String id,
      ProcessusDataProvider dataProvider,
      DecoratedCoreDataTablePanel<Processus, ?> table) {
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
        new TextField<>(
                "nom",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.processusSearchQueryData().nom()))
            .setLabel(new ResourceModel("business.processus.nom"))
            .add(new LabelPlaceholderBehavior()),
        new EnumDropDownSingleChoice<>(
                "thematique",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.processusSearchQueryData().thematique()),
                ProcessusThematique.class)
            .setLabel(new ResourceModel("business.processus.thematique"))
            .add(new LabelPlaceholderBehavior()),
        new EnumDropDownSingleChoice<>(
                "priorite",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.processusSearchQueryData().priorite()),
                ProcessusPriorite.class)
            .setLabel(new ResourceModel("business.processus.priorite"))
            .add(new LabelPlaceholderBehavior()));
  }
}
