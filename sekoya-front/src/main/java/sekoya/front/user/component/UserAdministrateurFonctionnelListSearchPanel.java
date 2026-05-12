package sekoya.front.user.component;

import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.wicketstuff.wiquery.core.events.StateEvent;
import sekoya.back.business.user.model.User;
import sekoya.back.util.binding.Bindings;
import sekoya.front.user.model.UserDataProvider;

public class UserAdministrateurFonctionnelListSearchPanel extends Panel {

  private static final long serialVersionUID = -4624527265796845060L;

  private final IModel<User> quickAccessModel = new GenericEntityModel<>();

  public UserAdministrateurFonctionnelListSearchPanel(
      String id, UserDataProvider dataProvider, DecoratedCoreDataTablePanel<User, ?> table) {
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
                "name",
                BindingModel.of(dataProvider.getDataModel(), Bindings.userSearchQueryData().term()))
            .setLabel(new ResourceModel("business.user.name"))
            .add(new LabelPlaceholderBehavior()),
        new EnumDropDownSingleChoice<>(
                "enabledFilter",
                BindingModel.of(
                    dataProvider.getDataModel(), Bindings.userSearchQueryData().active()),
                EnabledFilter.class)
            .setLabel(new ResourceModel("business.user.enabled.state"))
            .add(new LabelPlaceholderBehavior()));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(quickAccessModel);
  }
}
