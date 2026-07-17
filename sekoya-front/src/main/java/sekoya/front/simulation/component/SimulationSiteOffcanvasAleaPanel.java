package sekoya.front.simulation.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.service.IAleaService;
import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.service.business.ISimulationCalculService;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.component.ScoreRatingDisplayPanel;

public class SimulationSiteOffcanvasAleaPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IAleaService aleaService;

  @SpringBean private ISimulationCalculService simulationCalculService;

  public SimulationSiteOffcanvasAleaPanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<Alea> aleaModel,
      IModel<Boolean> impactPotentielBrutModeModel,
      IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);

    IModel<Risque> risqueModel =
        LoadableDetachableModel.of(
            () ->
                simulationCalculService.getAleaRisqueBrut(
                    aleaModel.getObject(), simulationSearchDtoModel.getObject()));

    IModel<Evolution> evolutionModel =
        LoadableDetachableModel.of(
            () ->
                simulationCalculService.getAleaEvolution(
                    aleaModel.getObject(), simulationSearchDtoModel.getObject()));

    add(
        new ScoreRatingDisplayPanel<>("risqueBrut", risqueModel),
        new CoreLabel("risqueBrutLabel", risqueModel).showPlaceholder());

    add(
        new ScoreRatingDisplayPanel<>(
            "impactPotentielBrut",
            BindingModel.of(aleaModel, Bindings.alea().impactPotentielBrut())),
        new CoreLabel(
                "impactPotentielBrutLabel",
                BindingModel.of(aleaModel, Bindings.alea().impactPotentielBrut()))
            .showPlaceholder(),
        new AjaxLink<>("view") {

          @Override
          public void onClick(AjaxRequestTarget target) {
            impactPotentielBrutModeModel.setObject(true);
            target.addChildren(getPage(), SimulationSiteOffcanvasContentPanel.class);
          }
        });

    add(
        new ScoreRatingDisplayPanel<>("evolution", evolutionModel),
        new CoreLabel("evolutionLabel", evolutionModel).showPlaceholder());

    add(
        Condition.and(
                Condition.modelNotNull(aleaModel),
                Condition.isTrue(impactPotentielBrutModeModel).negate())
            .thenShowInternal());
  }
}
