package sekoya.front.processus.component;

import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.ajax.AjaxListeners;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.business.alea.service.AleaImpactPotentielBrutCalculator;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusPriorite;
import sekoya.back.business.processus.model.atomic.ProcessusThematique;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.processus.service.controller.IProcessusControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.component.ScoreRatingFormCheckValuesPanel;
import sekoya.front.processus.model.ProcessusBindableModel;
import sekoya.front.site.form.SiteDropDownSingleChoice;

public class ProcessusSaveDescriptionPanel extends AbstractProcessusSavePanel {

  private static final long serialVersionUID = 1L;

  public static final Logger LOGGER = LoggerFactory.getLogger(ProcessusSaveDescriptionPanel.class);

  @SpringBean private IProcessusControllerService processusControllerService;

  public ProcessusSaveDescriptionPanel(String id, ProcessusBindableModel processusBindableModel) {
    super(id, processusBindableModel);
    setOutputMarkupId(true);

    Model<ProcessusThematique> thematiqueModel =
        Model.of(processusBindableModel.getObject().getThematique());

    add(
        new EnclosureContainer("unicity")
            .add(
                Condition.modelNotNull(
                        LoadableDetachableModel.of(
                            () ->
                                processusControllerService.getBySiteAndType(
                                    processusBindableModel
                                        .bind(Bindings.processus().site())
                                        .getObject(),
                                    processusBindableModel
                                        .bind(Bindings.processus().type())
                                        .getObject())))
                    .thenShow()));

    Form<Void> form = new Form<>("form");
    add(form);

    form.add(
        new SiteDropDownSingleChoice(
                "site", processusBindableModel.bind(Bindings.processus().site()))
            .setLabel(new ResourceModel("business.processus.site"))
            .setRequired(true)
            .add(Condition.modelNotNull(processusBindableModel.getSiteModel()).thenDisable())
            .add(
                new UpdateOnChangeAjaxEventBehavior()
                    .onChange(writeAll())
                    .onChange(AjaxListeners.refresh(ProcessusSaveDescriptionPanel.this))
                    .onChange(readAll())),
        new EnumDropDownSingleChoice<>("thematique", thematiqueModel, ProcessusThematique.class)
            .setLabel(new ResourceModel("business.processus.thematique"))
            .setRequired(true)
            .add(
                new UpdateOnChangeAjaxEventBehavior()
                    .onChange(writeAll())
                    .onChange(
                        new SerializableListener() {
                          @Override
                          public void onBeforeRespond(
                              Map<String, Component> map, AjaxRequestTarget target) {
                            Processus processus = processusBindableModel.getObject();
                            processus.setType(null);
                            processus.setNom(null);
                            target.add(ProcessusSaveDescriptionPanel.this);
                          }
                        })
                    .onChange(AjaxListeners.refresh(ProcessusSaveDescriptionPanel.this))
                    .onChange(readAll())),
        new EnumDropDownSingleChoice<>(
                "type",
                processusBindableModel.bind(Bindings.processus().type()),
                LoadableDetachableModel.of(
                    () -> ProcessusType.listByThematique(thematiqueModel.getObject())))
            .setLabel(new ResourceModel("business.processus.type"))
            .setRequired(true)
            .add(
                new UpdateOnChangeAjaxEventBehavior()
                    .onChange(writeAll())
                    .onChange(
                        new SerializableListener() {
                          @Override
                          public void onBeforeRespond(
                              Map<String, Component> map, AjaxRequestTarget target) {
                            Processus processus = processusBindableModel.getObject();
                            processus.setNom(
                                EnumRenderer.get().render(processus.getType(), getLocale()));
                            target.add(ProcessusSaveDescriptionPanel.this);
                          }
                        })
                    .onChange(readAll())),
        new TextField<>("nom", processusBindableModel.bind(Bindings.processus().nom()))
            .setLabel(new ResourceModel("business.processus.nom"))
            .setRequired(true)
            .add(new UpdateOnChangeAjaxEventBehavior()),
        new TextArea<>(
                "description", processusBindableModel.bind(Bindings.processus().description()))
            .setLabel(new ResourceModel("business.processus.description"))
            .add(new UpdateOnChangeAjaxEventBehavior()),
        new RadioGroup<>("priorite", processusBindableModel.bind(Bindings.processus().priorite()))
            .setLabel(new ResourceModel("business.processus.priorite"))
            .setRequired(true)
            .add(new ScoreRatingFormCheckValuesPanel<>("values", ProcessusPriorite.class))
            .add(
                new UpdateOnChangeAjaxEventBehavior()
                    .onChange(writeAll())
                    .onChange(
                        new SerializableListener() {
                          @Override
                          public void onBeforeRespond(
                              Map<String, Component> map, AjaxRequestTarget target) {
                            Processus processus = processusBindableModel.getObject();
                            processus
                                .getAleas()
                                .forEach(
                                    a ->
                                        a.setImpactPotentielBrut(
                                            AleaImpactPotentielBrutCalculator.generer(
                                                processus.getPriorite(), a.getSensibilite())));
                            processusBindableModel
                                .getAleaAddBindableModel()
                                .getObject()
                                .setImpactPotentielBrut(
                                    AleaImpactPotentielBrutCalculator.generer(
                                        processus.getPriorite(),
                                        processusBindableModel
                                            .getAleaAddBindableModel()
                                            .getObject()
                                            .getSensibilite()));
                            target.addChildren(getPage(), ProcessusSaveAleasPanel.class);
                          }
                        })
                    .onChange(readAll()))
            .setRenderBodyOnly(false));
  }
}
