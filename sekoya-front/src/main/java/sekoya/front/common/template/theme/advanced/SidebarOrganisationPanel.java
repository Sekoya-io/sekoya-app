package sekoya.front.common.template.theme.advanced;

import igloo.wicket.condition.Condition;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import sekoya.back.business.user.predicate.UserPredicates;
import sekoya.front.SekoyaSession;
import sekoya.front.navigation.page.HomePage;
import sekoya.front.organisation.form.OrganisationAjaxDropDownSingleChoice;

public class SidebarOrganisationPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public SidebarOrganisationPanel(String id) {
    super(id);

    add(
        Condition.predicate(SekoyaSession.get().getUserModel(), UserPredicates.administrateur())
            .thenShow());

    add(
        new OrganisationAjaxDropDownSingleChoice(
                "organisation", SekoyaSession.get().getOrganisationModel())
            .setLabel(new ResourceModel("sidebar.organisation.organisation"))
            .setRequired(true)
            .add(new LabelPlaceholderBehavior())
            .add(
                new UpdateOnChangeAjaxEventBehavior()
                    .onChange(
                        new SerializableListener() {
                          private static final long serialVersionUID = 1L;

                          @Override
                          public void onBeforeRespond(
                              Map<String, Component> map, AjaxRequestTarget target) {
                            throw HomePage.linkDescriptor().newRestartResponseException();
                          }
                        })));
  }
}
