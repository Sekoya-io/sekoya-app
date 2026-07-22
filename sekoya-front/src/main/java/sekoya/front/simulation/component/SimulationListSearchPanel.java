package sekoya.front.simulation.component;

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
import sekoya.back.business.referencedata.model.Region;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.model.atomic.SiteTypologie;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.form.ReferenceDataDropDownSingleChoice;
import sekoya.front.site.model.SiteDataProvider;

public class SimulationListSearchPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public SimulationListSearchPanel(
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
            target.addChildren(getPage(), SimulationSiteOffcanvasPanel.class);
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
        new ReferenceDataDropDownSingleChoice<>(
                "region",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.siteSearchQueryData().region()),
                Region.class)
            .setLabel(new ResourceModel("business.region"))
            .add(new LabelPlaceholderBehavior()));
  }
}
