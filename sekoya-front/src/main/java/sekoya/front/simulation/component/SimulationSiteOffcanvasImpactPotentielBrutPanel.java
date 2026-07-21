package sekoya.front.simulation.component;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import igloo.wicket.model.BindingModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.service.IAleaService;
import sekoya.back.business.donneeclimatique.service.IDonneeClimatiqueService;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.model.atomic.SimulationEtape;
import sekoya.back.business.simulation.service.business.ISimulationCalculService;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.component.ScoreRatingDisplayPanel;

public class SimulationSiteOffcanvasImpactPotentielBrutPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IAleaService aleaService;

  @SpringBean private IDonneeClimatiqueService donneeClimatiqueService;

  @SpringBean private ISimulationCalculService simulationCalculService;

  public SimulationSiteOffcanvasImpactPotentielBrutPanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<Alea> aleaModel,
      IModel<SimulationEtape> simulationEtapeModel,
      IModel<SimulationSearchDto> simulationSearchDtoModel) {
    super(id, siteModel);

    add(
        new WebMarkupContainer("aleaImpactPotentielBrutContainer")
            .add(
                new ScoreRatingDisplayPanel<>(
                    "impactPotentielBrut",
                    BindingModel.of(aleaModel, Bindings.alea().impactPotentielBrut())),
                new CoreLabel(
                        "impactPotentielBrutLabel",
                        BindingModel.of(aleaModel, Bindings.alea().impactPotentielBrut()))
                    .showPlaceholder())
            .add(
                new ClassAttributeAppender(
                    () ->
                        "rating-card-impact-potentiel-%s"
                            .formatted(
                                aleaModel.getObject().getImpactPotentielBrut().getScore()))));

    add(
        new WebMarkupContainer("processusPrioriteContainer")
            .add(
                new ScoreRatingDisplayPanel<>(
                    "priorite", BindingModel.of(aleaModel, Bindings.alea().processus().priorite())),
                new CoreLabel(
                        "prioriteLabel",
                        BindingModel.of(aleaModel, Bindings.alea().processus().priorite()))
                    .showPlaceholder())
            .add(
                new ClassAttributeAppender(
                    () ->
                        "rating-card-processus-priorite-%s"
                            .formatted(
                                aleaModel.getObject().getProcessus().getPriorite().getScore()))));

    add(
        new WebMarkupContainer("aleaSensibiliteContainer")
            .add(
                new ScoreRatingDisplayPanel<>(
                    "sensibilite", BindingModel.of(aleaModel, Bindings.alea().sensibilite())),
                new CoreLabel(
                        "sensibiliteLabel",
                        BindingModel.of(aleaModel, Bindings.alea().sensibilite()))
                    .showPlaceholder())
            .add(
                new ClassAttributeAppender(
                    () ->
                        "rating-card-alea-sensibilite-%s"
                            .formatted(aleaModel.getObject().getSensibilite().getScore()))));

    add(
        Condition.isEqual(simulationEtapeModel, Model.of(SimulationEtape.IMPACT_POTENTIEL))
            .thenShowInternal());
  }
}
