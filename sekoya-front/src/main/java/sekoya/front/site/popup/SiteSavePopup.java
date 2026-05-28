package sekoya.front.site.popup;

import igloo.bootstrap.modal.AbstractAjaxModalPopupPanel;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.panel.DelegatedMarkupPanel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import igloo.wicket.model.Models;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.ThrottlingSettings;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Joiners;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.ajax.SerializableListener;
import org.iglooproject.wicket.more.common.behavior.UpdateOnChangeAjaxEventBehavior;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.FormMode;
import org.iglooproject.wicket.more.markup.html.form.IndependentNestedForm;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sekoya.back.api.common.RestClientCommunicationException;
import sekoya.back.api.geocodage.bean.GeocodageGeocodeResponseBean;
import sekoya.back.api.geocodage.bean.GeocodageResponseBean;
import sekoya.back.api.geocodage.service.IGeocodageRestClientService;
import sekoya.back.business.common.model.CodePostal;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.service.controller.ICommuneControllerService;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.model.atomic.SiteTypologie;
import sekoya.back.business.site.service.controller.ISiteControllerService;
import sekoya.back.util.binding.Bindings;
import sekoya.front.SekoyaSession;
import sekoya.front.common.converter.PointConverter;
import sekoya.front.common.form.CommuneAjaxDropDownSingleChoice;
import sekoya.front.common.validator.SiteNomUnicityValidator;
import sekoya.front.site.page.SiteDetailPage;

public class SiteSavePopup extends AbstractAjaxModalPopupPanel<Site> {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(SiteSavePopup.class);

  @SpringBean private ISiteControllerService siteControllerService;

  @SpringBean private ICommuneControllerService communeControllerService;

  @SpringBean private IGeocodageRestClientService geocodageRestClientService;

  private final IModel<String> geocodeSearchTermModel = Model.of();
  private final IModel<GeocodageGeocodeResponseBean> geocodeResponseBeanModel = Model.of();
  private final IModel<List<GeocodageGeocodeResponseBean>> geocodesResponseBeanModel;
  private final IModel<Boolean> geocodageNoResultModel = Model.of(Boolean.FALSE);
  private final IModel<Boolean> geocodageErrorModel = Model.of(Boolean.FALSE);
  private final IModel<FormMode> formModeModel = Model.of();

  private Form<Site> form;

  public SiteSavePopup(String id) {
    super(id, new GenericEntityModel<>());

    this.geocodesResponseBeanModel =
        LoadableDetachableModel.of(
            () -> {
              String term = geocodeSearchTermModel.getObject();
              try {
                if (StringUtils.hasText(term)
                    && term.length() >= 3
                    && term.length() <= 200
                    && (Character.isLetter(term.charAt(0)) || Character.isDigit(term.charAt(0)))) {
                  GeocodageResponseBean responseBean = geocodageRestClientService.getSearch(term);
                  List<GeocodageGeocodeResponseBean> result = responseBean.getFeatures();
                  geocodageNoResultModel.setObject(result.isEmpty());
                  geocodageErrorModel.setObject(Boolean.FALSE);
                  return result;
                } else {
                  geocodageNoResultModel.setObject(Boolean.FALSE);
                  geocodageErrorModel.setObject(Boolean.FALSE);
                  return List.of();
                }
              } catch (RestClientCommunicationException e) {
                LOGGER.error("Erreur appel géocodage.");
                geocodageNoResultModel.setObject(Boolean.FALSE);
                geocodageErrorModel.setObject(Boolean.TRUE);
                return List.of();
              }
            });
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

    IModel<String> adresse1Model =
        BindingModel.of(getModel(), Bindings.site().adresse().adresse1());
    IModel<String> adresse2Model =
        BindingModel.of(getModel(), Bindings.site().adresse().adresse2());
    IModel<CodePostal> codePostalModel =
        BindingModel.of(getModel(), Bindings.site().adresse().codePostal());
    IModel<Commune> communeModel = BindingModel.of(getModel(), Bindings.site().adresse().commune());
    IModel<Point> localisationModel = BindingModel.of(getModel(), Bindings.site().localisation());

    EnclosureContainer geocodageChoicesContainer =
        new EnclosureContainer("geocodageChoicesContainer");
    EnclosureContainer adresseContainer = new EnclosureContainer("adresseContainer");

    form = new Form<>("form", getModel());
    body.add(form);

    WebMarkupContainer littoralContainer = new WebMarkupContainer("littoral");

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
                "chiffreAffaires",
                BindingModel.of(getModel(), Bindings.site().chiffreAffaires()),
                Integer.class)
            .setLabel(new ResourceModel("business.site.chiffreAffaires")),
        new IndependentNestedForm<>("geocodageSearchForm")
            .add(
                new TextField<>("term", geocodeSearchTermModel)
                    .setLabel(new ResourceModel("site.save.localisation.geocodage.search.term"))
                    .add(new LabelPlaceholderBehavior())
                    .add(
                        new AjaxFormComponentUpdatingBehavior("input") {
                          @Override
                          protected void onUpdate(AjaxRequestTarget target) {
                            target.add(geocodageChoicesContainer);
                            target.add(adresseContainer);
                          }

                          @Override
                          protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                            super.updateAjaxAttributes(attributes);
                            attributes.setThrottlingSettings(
                                new ThrottlingSettings(Duration.ofMillis(500), true));
                          }
                        })),
        geocodageChoicesContainer
            .condition(
                Condition.or(
                    Condition.collectionModelNotEmpty(geocodesResponseBeanModel),
                    Condition.isTrue(geocodageNoResultModel),
                    Condition.isTrue(geocodageErrorModel)))
            .add(
                new RadioGroup<>("choice", geocodeResponseBeanModel)
                    .setLabel(
                        new ResourceModel("site.save.localisation.geocodage.choices.radio.label"))
                    .add(
                        new CollectionView<>(
                            "choices",
                            geocodesResponseBeanModel,
                            Models.serializableModelFactory()) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void populateItem(Item<GeocodageGeocodeResponseBean> item) {
                            item.add(
                                new Radio<>("item", Model.of(item.getModelObject()))
                                    .setLabel(
                                        Model.of(
                                            item.getModelObject().getProperties().getLabel())));
                          }
                        }.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance()))
                    .add(
                        new UpdateOnChangeAjaxEventBehavior()
                            .onChange(
                                new SerializableListener() {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public void onBeforeRespond(
                                      Map<String, Component> map, AjaxRequestTarget target) {
                                    updateAdresseFields(
                                        geocodeResponseBeanModel.getObject(),
                                        adresse1Model,
                                        adresse2Model,
                                        codePostalModel,
                                        communeModel,
                                        localisationModel);
                                    target.add(adresseContainer);
                                  }
                                }))
                    .add(Condition.collectionModelNotEmpty(geocodesResponseBeanModel).thenShow())
                    .setRenderBodyOnly(false),
                new EnclosureContainer("noResultContainer")
                    .condition(Condition.isTrue(geocodageNoResultModel)),
                new EnclosureContainer("errorContainer")
                    .condition(Condition.isTrue(geocodageErrorModel)))
            .setOutputMarkupPlaceholderTag(true),
        adresseContainer
            .condition(
                Condition.or(
                    addModeCondition().negate(),
                    Condition.modelNotNull(geocodeResponseBeanModel),
                    Condition.isTrue(geocodageErrorModel)))
            .add(
                new TextField<>("adresse1", adresse1Model)
                    .setLabel(new ResourceModel("business.common.adresse.adresse1"))
                    .setRequired(true),
                new TextField<>("adresse2", adresse2Model)
                    .setLabel(new ResourceModel("business.common.adresse.adresse2")),
                new TextField<>("codePostal", codePostalModel, CodePostal.class)
                    .setLabel(new ResourceModel("business.common.adresse.codePostal"))
                    .setRequired(true),
                new CommuneAjaxDropDownSingleChoice("commune", communeModel)
                    .setLabel(new ResourceModel("business.common.adresse.commune"))
                    .setRequired(true),
                new TextField<>("localisation", localisationModel, Point.class)
                    .setLabel(
                        new ResourceModel("business.site.localisation.coordonneesGeographiques"))
                    .setRequired(true))
            .setOutputMarkupPlaceholderTag(true),
        littoralContainer
            .add(
                new RadioGroup<>(
                        "localisationLittoral",
                        BindingModel.of(
                            getModel(), Bindings.site().littoral().localisationLittoral()))
                    .setLabel(new ResourceModel(""))
                    .add(
                        new Radio<>("true", Model.of(true))
                            .setLabel(new ResourceModel("common.yes")),
                        new Radio<>("false", Model.of(false))
                            .setLabel(new ResourceModel("common.no")))
                    .add(
                        new UpdateOnChangeAjaxEventBehavior()
                            .onChange(
                                new SerializableListener() {
                                  @Override
                                  public void onBeforeRespond(
                                      Map<String, Component> map, AjaxRequestTarget target) {
                                    getModelObject().getLittoral().setZoneSubmersible(false);
                                    target.add(littoralContainer);
                                  }
                                }))
                    .setRenderBodyOnly(false),
                new RadioGroup<>(
                        "zoneSubmersible",
                        BindingModel.of(getModel(), Bindings.site().littoral().zoneSubmersible()))
                    .add(
                        new Radio<>("true", Model.of(true))
                            .setLabel(new ResourceModel("common.yes")),
                        new Radio<>("false", Model.of(false))
                            .setLabel(new ResourceModel("common.no")))
                    .add(
                        Condition.isTrue(
                                BindingModel.of(
                                    getModel(), Bindings.site().littoral().localisationLittoral()))
                            .thenShow())
                    .setRenderBodyOnly(false))
            .setOutputMarkupId(true));

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
              IModel<Site> siteModel = SiteSavePopup.this.getModel();
              Site site = siteModel.getObject();

              if (site.getOrganisation() == null) {
                site.setOrganisation(SekoyaSession.get().getOrganisationModel().getObject());
              }

              siteControllerService.saveSite(site);

              Session.get().success(getString("common.success"));

              throw SiteDetailPage.MAPPER.map(siteModel).newRestartResponseException();
            } catch (RestartResponseException e) { // NOSONAR
              throw e;
            } catch (Exception e) {
              LOGGER.error("Erreur saisie site", e);
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

  private void updateAdresseFields(
      GeocodageGeocodeResponseBean bean,
      IModel<String> adresse1Model,
      IModel<String> adresse2Model,
      IModel<CodePostal> codePostalModel,
      IModel<Commune> communeModel,
      IModel<Point> localisationModel) {
    if (bean.getProperties() != null) {
      adresse1Model.setObject(bean.getProperties().getName());
      adresse2Model.setObject(null);
      Commune commune =
          bean.getProperties().getCitycode() != null
              ? communeControllerService.getByCodeInsee(bean.getProperties().getCitycode())
              : null;
      communeModel.setObject(commune);
      codePostalModel.setObject(new CodePostal(bean.getProperties().getPostcode()));
    } else {
      adresse1Model.setObject(null);
      adresse2Model.setObject(null);
      communeModel.setObject(null);
      codePostalModel.setObject(null);
    }

    if (bean.getGeometry() != null && !bean.getGeometry().getCoordinates().isEmpty()) {
      localisationModel.setObject(
          PointConverter.get()
              .convertToObject(
                  Joiners.onComma()
                      .join(
                          bean.getGeometry().getCoordinates().getLast(),
                          bean.getGeometry().getCoordinates().getFirst()),
                  getLocale()));
    } else {
      localisationModel.setObject(null);
    }
  }

  @Override
  protected void onShow(AjaxRequestTarget target) {
    super.onShow(target);

    geocodeSearchTermModel.setObject(null);
    geocodeResponseBeanModel.setObject(null);
    geocodesResponseBeanModel.setObject(List.of());
    geocodageErrorModel.setObject(Boolean.FALSE);
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
  public IModel<String> getModalDialogCssClassModel() {
    return Model.of("modal-dialog-scrollable");
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(
        geocodeSearchTermModel,
        geocodeResponseBeanModel,
        geocodesResponseBeanModel,
        geocodageNoResultModel,
        geocodageErrorModel,
        formModeModel);
  }
}
