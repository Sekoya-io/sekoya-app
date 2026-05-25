package sekoya.front.processus.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.util.binding.Bindings;
import sekoya.front.site.page.SiteDetailPage;

public class ProcessusDetailDescriptionPanel extends GenericPanel<Processus> {

  private static final long serialVersionUID = 1L;

  public ProcessusDetailDescriptionPanel(String id, final IModel<Processus> processusModel) {
    super(id, processusModel);

    add(
        SiteDetailPage.MAPPER
            .map(BindingModel.of(processusModel, Bindings.processus().site()))
            .link("siteLink")
            .add(
                new CoreLabel("site", BindingModel.of(processusModel, Bindings.processus().site()))
                    .showPlaceholder()),
        new CoreLabel(
                "thematique", BindingModel.of(processusModel, Bindings.processus().thematique()))
            .showPlaceholder(),
        new CoreLabel("type", BindingModel.of(processusModel, Bindings.processus().type()))
            .showPlaceholder(),
        new CoreLabel(
                "description", BindingModel.of(processusModel, Bindings.processus().description()))
            .showPlaceholder()
            .multiline(),
        new ProcessusPrioriteRatingDisplayPanel(
                "priorite", BindingModel.of(processusModel, Bindings.processus().priorite()))
            .small(),
        new CoreLabel("creation", BindingModel.of(processusModel, Bindings.processus().creation()))
            .showPlaceholder(),
        new CoreLabel(
                "modification",
                BindingModel.of(processusModel, Bindings.processus().modification()))
            .showPlaceholder());
  }
}
