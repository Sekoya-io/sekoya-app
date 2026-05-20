package sekoya.front.site.component;

import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_WRITE;

import igloo.bootstrap.modal.AjaxModalOpenBehavior;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.wicketstuff.wiquery.core.events.MouseEvent;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.renderer.CommonRenderers;
import sekoya.front.site.popup.SiteSavePopup;

public class SiteDetailDescriptionPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SiteDetailDescriptionPanel(String id, final IModel<Site> siteModel) {
    super(id, siteModel);

    SiteSavePopup editPopup = new SiteSavePopup("editPopup");
    add(editPopup);

    add(
        new CoreLabel("typologie", BindingModel.of(siteModel, Bindings.site().typologie()))
            .showPlaceholder(),
        new CoreLabel(
                "chiffreAffaires",
                CommonRenderers.kiloEuros()
                    .asModel(BindingModel.of(siteModel, Bindings.site().chiffreAffaires())))
            .showPlaceholder(),
        new CoreLabel("adresse", BindingModel.of(siteModel, Bindings.site().adresse()))
            .showPlaceholder()
            .multiline(),
        new CoreLabel("latitude", BindingModel.of(siteModel, Bindings.site().latitude()))
            .showPlaceholder(),
        new CoreLabel("longitude", BindingModel.of(siteModel, Bindings.site().longitude()))
            .showPlaceholder(),
        new CoreLabel("creation", BindingModel.of(siteModel, Bindings.site().creation()))
            .showPlaceholder(),
        new CoreLabel("modification", BindingModel.of(siteModel, Bindings.site().modification()))
            .showPlaceholder());

    add(
        new EnclosureContainer("actionsContainer")
            .anyChildVisible()
            .add(
                new BlankLink("edit")
                    .add(
                        new AjaxModalOpenBehavior(editPopup, MouseEvent.CLICK) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void onShow(AjaxRequestTarget target) {
                            editPopup.setUpEdit(getModelObject());
                          }
                        })
                    .add(Condition.permission(siteModel, SITE_WRITE).thenShow())));
  }
}
