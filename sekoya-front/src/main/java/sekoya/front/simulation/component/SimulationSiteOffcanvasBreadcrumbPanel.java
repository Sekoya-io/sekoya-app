package sekoya.front.simulation.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.site.model.Site;

public class SimulationSiteOffcanvasBreadcrumbPanel extends GenericPanel<Site> {

  private static final long serialVersionUID = 1L;

  public SimulationSiteOffcanvasBreadcrumbPanel(
      String id,
      IModel<Site> siteModel,
      IModel<Processus> processusModel,
      IModel<Alea> aleaModel,
      IModel<Boolean> impactPotentielBrutModeModel) {
    super(id, siteModel);

    add(
        new AjaxLink<Void>("siteLink") {

          @Override
          public void onClick(AjaxRequestTarget target) {
            processusModel.setObject(null);
            aleaModel.setObject(null);
            impactPotentielBrutModeModel.setObject(null);
            target.addChildren(getPage(), SimulationSiteOffcanvasContentPanel.class);
          }
        },
        new EnclosureContainer("processusContainer")
            .anyChildVisible()
            .add(
                new AjaxLink<Void>("processusLink") {

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    aleaModel.setObject(null);
                    impactPotentielBrutModeModel.setObject(null);
                    target.addChildren(getPage(), SimulationSiteOffcanvasContentPanel.class);
                  }
                }.add(new CoreLabel("processus", processusModel).showPlaceholder())
                    .add(
                        Condition.and(
                                Condition.modelNotNull(processusModel),
                                Condition.modelNotNull(aleaModel))
                            .thenShow()),
                new CoreLabel("processus", processusModel)
                    .showPlaceholder()
                    .add(
                        Condition.and(
                                Condition.modelNotNull(processusModel),
                                Condition.modelNotNull(aleaModel).negate())
                            .thenShow())),
        new EnclosureContainer("aleaContainer")
            .anyChildVisible()
            .add(
                new AjaxLink<Void>("aleaLink") {

                  @Override
                  public void onClick(AjaxRequestTarget target) {
                    impactPotentielBrutModeModel.setObject(null);
                    target.addChildren(getPage(), SimulationSiteOffcanvasContentPanel.class);
                  }
                }.add(new CoreLabel("alea", aleaModel).showPlaceholder())
                    .add(
                        Condition.and(
                                Condition.modelNotNull(aleaModel),
                                Condition.isTrue(impactPotentielBrutModeModel))
                            .thenShow()),
                new CoreLabel("alea", aleaModel)
                    .showPlaceholder()
                    .add(
                        Condition.and(
                                Condition.modelNotNull(aleaModel),
                                Condition.isTrue(impactPotentielBrutModeModel).negate())
                            .thenShow())),
        new EnclosureContainer("impactPotentielBrutContainer")
            .condition(Condition.isTrue(impactPotentielBrutModeModel)));

    add(Condition.modelNotNull(processusModel).thenShowInternal());
  }
}
