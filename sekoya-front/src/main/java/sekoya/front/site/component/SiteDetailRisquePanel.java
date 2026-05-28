package sekoya.front.site.component;

import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.component.RisqueRatingDisplayPanel;

public class SiteDetailRisquePanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SiteDetailRisquePanel(String id, IModel<Site> siteModel) {
    super(id, siteModel);

    add(
        new RisqueRatingDisplayPanel(
                "risqueBrutRcp45Annee2035",
                BindingModel.of(siteModel, Bindings.site().risqueBrutRcp45Annee2035()))
            .small(),
        new RisqueRatingDisplayPanel(
                "risqueBrutRcp45Annee2055",
                BindingModel.of(siteModel, Bindings.site().risqueBrutRcp45Annee2055()))
            .small(),
        new RisqueRatingDisplayPanel(
                "risqueBrutRcp85Annee2035",
                BindingModel.of(siteModel, Bindings.site().risqueBrutRcp85Annee2035()))
            .small(),
        new RisqueRatingDisplayPanel(
                "risqueBrutRcp85Annee2055",
                BindingModel.of(siteModel, Bindings.site().risqueBrutRcp85Annee2055()))
            .small());
  }
}
