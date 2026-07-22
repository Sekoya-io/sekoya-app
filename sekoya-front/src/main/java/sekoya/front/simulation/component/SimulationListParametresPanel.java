package sekoya.front.simulation.component;

import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Models;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.wicketstuff.wiquery.core.events.StateEvent;
import sekoya.back.business.common.model.atomic.Horizon;
import sekoya.back.business.common.model.atomic.Scenario;
import sekoya.back.business.simulation.dto.SimulationParametresDto;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.site.model.SiteDataProvider;

public class SimulationListParametresPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public SimulationListParametresPanel(
      String id,
      SiteDataProvider dataProvider,
      DecoratedCoreDataTablePanel<Site, ?> table,
      IModel<SimulationParametresDto> simulationParametresDtoModel) {
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
