package sekoya.front.simulation.component;

import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Models;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.wicketstuff.wiquery.core.events.StateEvent;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.simulation.dto.SimulationParametresDto;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.map.component.MapPanel;

public class SimulationMapParametresPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public SimulationMapParametresPanel(
      String id, IModel<SimulationParametresDto> simulationParametresDtoModel) {
    super(id);
    setOutputMarkupId(true);

    Form<Void> form = new Form<>("form");
    add(form);

    form.add(
        new AjaxFormSubmitBehavior(form, StateEvent.CHANGE.getEventLabel()) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            getPage()
                .visitChildren(
                    MapPanel.class,
                    (MapPanel mapPanel, IVisit<Void> visit) -> {
                      mapPanel.updatePoints(target);
                      visit.stop();
                    });
            getPage()
                .visitChildren(
                    SimulationSiteOffcanvasPanel.class,
                    (SimulationSiteOffcanvasPanel offcanvasPanel, IVisit<Void> visit) -> {
                      offcanvasPanel.reset();
                      visit.stop();
                    });
            target.addChildren(getPage(), SimulationSiteOffcanvasHeaderPanel.class);
            target.addChildren(getPage(), SimulationSiteOffcanvasBodyPanel.class);
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        });

    form.add(
        new RadioGroup<>(
                "scenario",
                BindingModel.of(
                    simulationParametresDtoModel, Bindings.simulationParametresDto().scenario()))
            .setLabel(new ResourceModel("business.common.scenario"))
            .setRequired(true)
            .add(
                new CollectionView<>(
                    "values",
                    Model.of(Arrays.asList(Scenario.values())),
                    Models.serializableModelFactory()) {

                  @Override
                  protected void populateItem(Item<Scenario> item) {
                    item.add(
                        new Radio<>("value", item.getModel())
                            .setLabel(EnumRenderer.get().asModel(item.getModel())));
                  }
                })
            .setRenderBodyOnly(false),
        new RadioGroup<>(
                "horizon",
                BindingModel.of(
                    simulationParametresDtoModel, Bindings.simulationParametresDto().horizon()))
            .setLabel(new ResourceModel("business.common.horizon"))
            .setRequired(true)
            .add(
                new CollectionView<>(
                    "values",
                    Model.of(Arrays.asList(Horizon.values())),
                    Models.serializableModelFactory()) {

                  @Override
                  protected void populateItem(Item<Horizon> item) {
                    item.add(
                        new Radio<>("value", item.getModel())
                            .setLabel(EnumRenderer.get().asModel(item.getModel())));
                  }
                })
            .setRenderBodyOnly(false),
        new RadioGroup<>(
                "enableProcessus",
                BindingModel.of(
                    simulationParametresDtoModel,
                    Bindings.simulationParametresDto().enableProcessus()))
            .setLabel(new ResourceModel("simulation.common.wording.enableProcessus"))
            .setRequired(true)
            .add(
                new CollectionView<>(
                    "values",
                    Model.of(List.of(Boolean.TRUE, Boolean.FALSE)),
                    Models.serializableModelFactory()) {

                  @Override
                  protected void populateItem(Item<Boolean> item) {
                    item.add(
                        new Radio<>("value", item.getModel())
                            .setLabel(BooleanRenderer.yesNo().asModel(item.getModel())));
                  }
                })
            .setRenderBodyOnly(false));
  }
}
