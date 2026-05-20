package sekoya.front.processus.component;

import com.google.common.collect.Lists;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import java.util.List;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.wicket.more.ajax.AjaxListeners;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.bindable.component.BindableCollectionView;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.IndependentNestedForm;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.collection.SpecificModelCollectionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.alea.service.AleaImpactPotentielBrutCalculator;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.util.binding.Bindings;
import sekoya.front.processus.model.ProcessusBindableModel;

public class ProcessusSaveAleasPanel extends AbstractProcessusSavePanel {

  private static final long serialVersionUID = 1L;

  public static final Logger LOGGER = LoggerFactory.getLogger(ProcessusSaveAleasPanel.class);

  public ProcessusSaveAleasPanel(String id, ProcessusBindableModel processusBindableModel) {
    super(id, processusBindableModel);
    setOutputMarkupId(true);

    Form<Void> form = new Form<>("form");
    add(form);

    form.add(
        new BindableCollectionView<Alea>(
            "aleas",
            processusBindableModel.bindCollectionAlreadyAdded(Bindings.processus().aleas())) {

          @Override
          protected void populateItem(
              SpecificModelCollectionView<Alea, IBindableModel<Alea>>.SpecificModelItem item) {
            item.add(new RowFragment("row", item.getSpecificModel()));
          }
        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()));

    add(new AddFragment("add"));
  }

  private class RowFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public RowFragment(String id, IBindableModel<Alea> aleaBindableModel) {
      super(id, "rowFragment", ProcessusSaveAleasPanel.this, aleaBindableModel);

      add(
          new CoreLabel("type", aleaBindableModel.bind(Bindings.alea().type())),
          new RadioGroup<>("sensibilite", aleaBindableModel.bind(Bindings.alea().sensibilite()))
              .setLabel(new ResourceModel("business.alea.sensibilite"))
              .setRequired(true)
              .add(new AleaSensibiliteRatingFormCheckValuesPanel("values"))
              .add(
                  new UpdateOnChangeAjaxEventBehavior()
                      .onChange(writeAll())
                      .onChange(
                          new SerializableListener() {
                            @Override
                            public void onBeforeRespond(
                                Map<String, Component> map, AjaxRequestTarget target) {
                              Processus processus = processusBindableModel.getObject();
                              Alea alea = aleaBindableModel.getObject();
                              alea.setImpactPotentielBrut(
                                  AleaImpactPotentielBrutCalculator.generer(
                                      processus.getPriorite(), alea.getSensibilite()));
                              target.addChildren(getPage(), ProcessusSaveAleasPanel.class);
                            }
                          })
                      .onChange(readAll()))
              .setRenderBodyOnly(false),
          new AleaImpactPotentielBrutRatingDisplayPanel(
              "impactPotentielBrut", aleaBindableModel.bind(Bindings.alea().impactPotentielBrut())),
          new AjaxLink<>("delete") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
              AjaxListeners.add(target, writeAll());
              AjaxListeners.add(
                  target,
                  new SerializableListener() {
                    @Override
                    public void onBeforeRespond(
                        Map<String, Component> map, AjaxRequestTarget target) {
                      Processus processus = processusBindableModel.getObject();
                      processus.removeAlea(aleaBindableModel.getObject());
                      target.add(ProcessusSaveAleasPanel.this);
                    }
                  });
              AjaxListeners.add(target, readAll());
            }
          });
    }
  }

  private class AddFragment extends Fragment {

    private static final long serialVersionUID = 1L;

    public AddFragment(String id) {
      super(id, "addFragment", ProcessusSaveAleasPanel.this);

      IModel<List<AleaType>> typesDisponiblesModel =
          LoadableDetachableModel.of(
              () -> {
                List<AleaType> aleaTypesDisponibles = Lists.newArrayList(AleaType.values());
                aleaTypesDisponibles.removeAll(
                    processusBindableModel.getObject().getAleas().stream()
                        .map(Bindings.alea().type())
                        .toList());
                return List.copyOf(aleaTypesDisponibles);
              });

      add(
          new IndependentNestedForm<>("add")
              .add(
                  new EnumDropDownSingleChoice<>(
                          "type",
                          processusBindableModel
                              .getAleaAddBindableModel()
                              .bind(Bindings.alea().type()),
                          typesDisponiblesModel)
                      .setLabel(new ResourceModel("business.alea.type"))
                      .add(new LabelPlaceholderBehavior())
                      .add(
                          new UpdateOnChangeAjaxEventBehavior()
                              .onChange(writeAll())
                              .onChange(AjaxListeners.refresh(ProcessusSaveAleasPanel.this))
                              .onChange(readAll())),
                  new RadioGroup<>(
                          "sensibilite",
                          processusBindableModel
                              .getAleaAddBindableModel()
                              .bind(Bindings.alea().sensibilite()))
                      .setLabel(new ResourceModel("business.alea.sensibilite"))
                      .setRequired(true)
                      .add(new AleaSensibiliteRatingFormCheckValuesPanel("values"))
                      .add(
                          new UpdateOnChangeAjaxEventBehavior()
                              .onChange(writeAll())
                              .onChange(
                                  new SerializableListener() {
                                    @Override
                                    public void onBeforeRespond(
                                        Map<String, Component> map, AjaxRequestTarget target) {
                                      Processus processus = processusBindableModel.getObject();
                                      Alea alea =
                                          processusBindableModel
                                              .getAleaAddBindableModel()
                                              .getObject();
                                      alea.setImpactPotentielBrut(
                                          AleaImpactPotentielBrutCalculator.generer(
                                              processus.getPriorite(), alea.getSensibilite()));
                                      target.addChildren(getPage(), ProcessusSaveAleasPanel.class);
                                    }
                                  })
                              .onChange(readAll()))
                      .setRenderBodyOnly(false),
                  new AjaxButton("add") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onSubmit(AjaxRequestTarget target) {
                      AjaxListeners.add(target, writeAll());
                      AjaxListeners.add(
                          target,
                          new SerializableListener() {
                            @Override
                            public void onBeforeRespond(
                                Map<String, Component> map, AjaxRequestTarget target) {
                              Processus processus = processusBindableModel.getObject();
                              processus.addAlea(
                                  processusBindableModel.getAleaAddBindableModel().getObject());
                              processusBindableModel.initAleaAddBindableModel();
                              typesDisponiblesModel.detach();
                              target.add(ProcessusSaveAleasPanel.this);
                            }
                          });
                      AjaxListeners.add(target, readAll());
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target) {
                      AjaxListeners.add(target, AjaxListeners.clearInput(getForm()));
                      FeedbackUtils.refreshFeedback(target, getPage());
                    }
                  })
              .add(Condition.collectionModelNotEmpty(typesDisponiblesModel).thenShow()));
    }
  }
}
