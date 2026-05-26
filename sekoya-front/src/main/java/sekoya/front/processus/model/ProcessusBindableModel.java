package sekoya.front.processus.model;

import igloo.wicket.model.Detachables;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.bindable.model.BindableModel;
import org.iglooproject.wicket.more.bindable.model.IBindableModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.comparator.AleaComparator;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.site.model.Site;
import sekoya.back.util.binding.Bindings;

public class ProcessusBindableModel extends BindableModel<Processus> {

  private static final long serialVersionUID = 1L;

  private final IModel<Site> siteModel = new GenericEntityModel<>();

  private final IBindableModel<Alea> aleaAddBindableModel =
      new BindableModel<>(new GenericEntityModel<>());

  public ProcessusBindableModel(IModel<Processus> participationModel) {
    super(participationModel);

    bindWithCache(Bindings.processus().site(), new GenericEntityModel<>());
    bindWithCache(Bindings.processus().type(), Model.of());
    bindWithCache(Bindings.processus().nom(), Model.of());
    bindWithCache(Bindings.processus().description(), Model.of());
    bindWithCache(Bindings.processus().priorite(), Model.of());

    bindCollectionWithCache(
        Bindings.processus().aleas(),
        Suppliers2.treeSetAsSortedSet(AleaComparator.get()),
        alea -> {
          var aleaBindableModel = new BindableModel<>(GenericEntityModel.of(alea));
          aleaBindableModel.bindWithCache(Bindings.alea().type(), Model.of());
          aleaBindableModel.bindWithCache(Bindings.alea().sensibilite(), Model.of());
          aleaBindableModel.bindWithCache(Bindings.alea().impactPotentielBrut(), Model.of());
          return aleaBindableModel;
        });

    initAleaAddBindableModel();
  }

  public IModel<Site> getSiteModel() {
    return siteModel;
  }

  public IBindableModel<Alea> getAleaAddBindableModel() {
    return aleaAddBindableModel;
  }

  public void initAleaAddBindableModel() {
    Alea alea = new Alea();
    alea.setProcessus(getObject());
    aleaAddBindableModel.setObject(alea);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(siteModel, aleaAddBindableModel);
  }
}
