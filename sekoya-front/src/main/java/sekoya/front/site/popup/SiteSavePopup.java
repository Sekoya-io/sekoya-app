package sekoya.front.site.popup;

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
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.business.common.model.CodePostal;
import sekoya.back.business.common.model.Latitude;
import sekoya.back.business.common.model.Longitude;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.model.atomic.SiteTypologie;
import sekoya.back.business.site.service.controller.ISiteControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.SekoyaSession;
import sekoya.front.common.form.CommuneAjaxDropDownSingleChoice;
import sekoya.front.common.validator.SiteNomUnicityValidator;

public class SiteSavePopup extends AbstractAjaxModalPopupPanel<Site> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(SiteSavePopup.class);

  @SpringBean private ISiteControllerService siteControllerService;

  private final IModel<FormMode> formModeModel = Model.of();

  private Form<Site> form;

  public SiteSavePopup(String id) {
    super(id, new GenericEntityModel<>());
  }

  @Override
  protected Component createHeader(String wicketId) {
    return new CoreLabel(
        wicketId,
        addModeCondition()
            .then(new ResourceModel("site.add.title"))
            .otherwise(new StringResourceModel("site.edit.title", getModel())));
  }

  @Override
  protected Component createBody(String wicketId) {
    DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());

    form = new Form<>("form", getModel());
    body.add(form);

    form.add(
        new TextField<>("nom", BindingModel.of(getModel(), Bindings.site().nom()))
            .setLabel(new ResourceModel("business.site.nom"))
            .setRequired(true)
            .add(new SiteNomUnicityValidator(getModel())),
        new EnumDropDownSingleChoice<>(
                "typologie",
                BindingModel.of(getModel(), Bindings.site().typologie()),
                SiteTypologie.class)
            .setLabel(new ResourceModel("business.site.typologie"))
            .setRequired(true),
        new TextField<>(
                "adresse1", BindingModel.of(getModel(), Bindings.site().adresse().adresse1()))
            .setLabel(new ResourceModel("business.common.adresse.adresse1"))
            .setRequired(true),
        new TextField<>(
                "adresse2", BindingModel.of(getModel(), Bindings.site().adresse().adresse2()))
            .setLabel(new ResourceModel("business.common.adresse.adresse2")),
        new TextField<>(
                "codePostal",
                BindingModel.of(getModel(), Bindings.site().adresse().codePostal()),
                CodePostal.class)
            .setLabel(new ResourceModel("business.common.adresse.codePostal"))
            .setRequired(true),
        new CommuneAjaxDropDownSingleChoice(
                "commune", BindingModel.of(getModel(), Bindings.site().adresse().commune()))
            .setLabel(new ResourceModel("business.common.adresse.commune"))
            .setRequired(true),
        new TextField<>(
                "latitude", BindingModel.of(getModel(), Bindings.site().latitude()), Latitude.class)
            .setLabel(new ResourceModel("business.site.latitude"))
            .setRequired(true),
        new TextField<>(
                "longitude",
                BindingModel.of(getModel(), Bindings.site().longitude()),
                Longitude.class)
            .setLabel(new ResourceModel("business.site.longitude"))
            .setRequired(true),
        new TextField<>(
                "chiffreAffaires",
                BindingModel.of(getModel(), Bindings.site().chiffreAffaires()),
                Integer.class)
            .setLabel(new ResourceModel("business.site.chiffreAffaires"))
            .setRequired(true));

    return body;
  }

  @Override
  protected Component createFooter(String wicketId) {
    DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, SiteSavePopup.class);

    footer.add(
        new AjaxButton("save", form) {
          private static final long serialVersionUID = 1L;

          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              Site site = SiteSavePopup.this.getModelObject();

              if (site.getOrganisation() == null) {
                site.setOrganisation(SekoyaSession.get().getOrganisationModel().getObject());
              }

              siteControllerService.saveSite(site);

              Session.get().success(getString("common.success"));

              closePopup(target);
              target.add(getPage());
            } catch (Exception e) {
              LOGGER.error("Erreur lors de la saisie d'un site", e);
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

  public void setUpAdd(Site site) {
    formModeModel.setObject(FormMode.ADD);
    getModel().setObject(site);
  }

  public void setUpEdit(Site site) {
    formModeModel.setObject(FormMode.EDIT);
    getModel().setObject(site);
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
