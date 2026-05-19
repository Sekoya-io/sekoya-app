package sekoya.front.site.component;

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
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.model.atomic.SiteTypologie;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.form.CommuneAjaxDropDownSingleChoice;
import sekoya.front.site.model.SiteDataProvider;

public class SiteListSearchPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public SiteListSearchPanel(
      String id, SiteDataProvider dataProvider, DecoratedCoreDataTablePanel<Site, ?> table) {
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
                BindingModel.of(dataProvider.getDataModel(), Bindings.siteSearchQueryData().nom()))
            .setLabel(new ResourceModel("business.site.nom"))
            .add(new LabelPlaceholderBehavior()),
        new EnumDropDownSingleChoice<>(
                "typologie",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.siteSearchQueryData().typologie()),
                SiteTypologie.class)
            .setLabel(new ResourceModel("business.site.typologie"))
            .add(new LabelPlaceholderBehavior()),
        new CommuneAjaxDropDownSingleChoice(
                "commune",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.siteSearchQueryData().commune()))
            .setLabel(new ResourceModel("business.common.adresse.commune"))
            .add(new LabelPlaceholderBehavior()),
        new EnumDropDownSingleChoice<>(
                "enabledFilter",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.siteSearchQueryData().enabledFilter()),
                EnabledFilter.class)
            .setLabel(new ResourceModel("business.common.enabled.true"))
            .add(new LabelPlaceholderBehavior()));
  }
}
