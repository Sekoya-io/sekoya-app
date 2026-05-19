package sekoya.front.referencedata.component;

import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.wicketstuff.wiquery.core.events.StateEvent;
import sekoya.back.business.referencedata.model.Departement;
import sekoya.back.business.referencedata.model.Region;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.behavior.FormCancelDefaultSubmitBehavior;
import sekoya.front.common.form.ReferenceDataDropDownSingleChoice;
import sekoya.front.referencedata.model.DepartementDataProvider;

public class DepartementListSearchPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public DepartementListSearchPanel(
      String id,
      DepartementDataProvider dataProvider,
      DecoratedCoreDataTablePanel<Departement, ?> table) {
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
        },
        new FormCancelDefaultSubmitBehavior());

    form.add(
        new TextField<String>(
                "label",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.departementSearchQueryData().label()))
            .setLabel(new ResourceModel("business.referenceData.label"))
            .add(new LabelPlaceholderBehavior()),
        new TextField<>(
                "codeInsee",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.departementSearchQueryData().codeInsee()))
            .setLabel(new ResourceModel("business.departement.codeInsee"))
            .add(new LabelPlaceholderBehavior()),
        new ReferenceDataDropDownSingleChoice<>(
                "region",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.departementSearchQueryData().region()),
                Region.class)
            .setLabel(new ResourceModel("business.region"))
            .add(new LabelPlaceholderBehavior()),
        new EnumDropDownSingleChoice<>(
                "enabledFilter",
                BindingModel.of(
                    dataProvider.getDataModel(),
                    Bindings.departementSearchQueryData().enabledFilter()),
                EnabledFilter.class)
            .setLabel(new ResourceModel("business.common.enabled.true"))
            .add(new LabelPlaceholderBehavior()));
  }
}
