package sekoya.front.organisation.popup;

import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.organisation.service.controller.IOrganisationControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.validator.OrganisationNomUnicityValidator;

public class OrganisationSavePopup extends AbstractAjaxModalPopupPanel<Organisation> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationSavePopup.class);

  @SpringBean private IOrganisationControllerService organisationControllerService;

  private final IModel<FormMode> formModeModel = Model.of();

  private Form<Organisation> form;

  public OrganisationSavePopup(String id) {
    super(id, new GenericEntityModel<>());
  }

  @Override
  protected Component createHeader(String wicketId) {
    return new CoreLabel(
        wicketId,
        addModeCondition()
            .then(new ResourceModel("organisation.add.title"))
            .otherwise(new StringResourceModel("organisation.edit.title", getModel())));
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    form = new Form<>("form", getModel());
    body.add(form);

    form.add(
        new TextField<>("nom", BindingModel.of(getModel(), Bindings.organisation().nom()))
            .setLabel(new ResourceModel("business.organisation.nom"))
            .setRequired(true)
            .add(new OrganisationNomUnicityValidator(getModel())),
        new TextField<>(
                "chiffreAffaires",
                BindingModel.of(getModel(), Bindings.organisation().chiffreAffaires()),
                Integer.class)
            .setLabel(new ResourceModel("business.organisation.chiffreAffaires"))
            .setRequired(true));

    return body;
  }

  @Override
  protected Component createFooter(String wicketId) {
    DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, OrganisationSavePopup.class);

    footer.add(
        new AjaxButton("save", form) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              Organisation organisation = OrganisationSavePopup.this.getModelObject();

              organisationControllerService.saveOrganisation(organisation);

              Session.get().success(getString("common.success"));

              closePopup(target);
              target.add(getPage());
            } catch (Exception e) {
              LOGGER.error("Erreur lors de la saisie d'une organisation", e);
              Session.get().error(getString("common.error.unexpected"));
            }
            FeedbackUtils.refreshFeedback(target, getPage());
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
            FeedbackUtils.refreshFeedback(target, getPage());
          }
        });

    BlankLink cancel = new BlankLink("cancel");
    addCancelBehavior(cancel);
    footer.add(cancel);

    return footer;
  }

  public void setUpAdd(Organisation organisation) {
    formModeModel.setObject(FormMode.ADD);
    getModel().setObject(organisation);
  }

  public void setUpEdit(Organisation organisation) {
    formModeModel.setObject(FormMode.EDIT);
    getModel().setObject(organisation);
  }

  private Condition addModeCondition() {
    return FormMode.ADD.condition(formModeModel);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(formModeModel);
  }
}
