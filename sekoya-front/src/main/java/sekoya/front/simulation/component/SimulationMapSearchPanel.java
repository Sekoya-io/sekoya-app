package sekoya.front.simulation.component;

import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.wicketstuff.wiquery.core.events.StateEvent;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.map.component.MapPanel;

public class SimulationMapSearchPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public SimulationMapSearchPanel(String id, IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id);

    Form<Void> form = new Form<>("form");
    add(form);

    form.add(
        new AjaxFormSubmitBehavior(form, StateEvent.CHANGE.getEventLabel()) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            // TODO : refresh plus fin ?
            target.addChildren(getPage(), MapPanel.class);
            target.addChildren(getPage(), SimulationSiteOffcanvasPanel.class);
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        });

    form.add(
        new EnumDropDownSingleChoice<>(
                "scenario",
                BindingModel.of(
                    simulationSearchDtoModel, Bindings.simulationSearchDto().scenario()),
                Scenario.class)
            .setLabel(new ResourceModel("business.common.scenario"))
            .setRequired(true)
            .add(new LabelPlaceholderBehavior()),
        new EnumDropDownSingleChoice<>(
                "horizon",
                BindingModel.of(simulationSearchDtoModel, Bindings.simulationSearchDto().horizon()),
                Horizon.class)
            .setLabel(new ResourceModel("business.common.horizon"))
            .setRequired(true)
            .add(new LabelPlaceholderBehavior()),
        // TODO : disable si pas de processus avec aléa ?
        // Comment gérer le refresh ?
        new CheckBox(
                "applyProcessus",
                BindingModel.of(
                    simulationSearchDtoModel, Bindings.simulationSearchDto().applyProcessus()))
            .setLabel(new ResourceModel("simulation.common.applyProcessus"))
            .setRequired(true)
            .setOutputMarkupId(true));
  }
}
