package sekoya.front.site.form;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.form.GenericEntityRendererToChoiceRenderer;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.service.controller.ISiteControllerService;
import sekoya.front.SekoyaSession;
import sekoya.front.site.renderer.SiteRenderer;

public class SiteDropDownSingleChoice extends GenericSelect2DropDownSingleChoice<Site> {

  private static final long serialVersionUID = 1L;

  public SiteDropDownSingleChoice(String id, IModel<Site> model) {
    this(id, model, UnaryOperator.identity());
  }

  public SiteDropDownSingleChoice(
      String id, IModel<Site> model, IModel<? extends Collection<Site>> sitesModel) {
    super(id, model, sitesModel, GenericEntityRendererToChoiceRenderer.of(SiteRenderer.get()));
  }

  public SiteDropDownSingleChoice(
      String id, IModel<Site> model, UnaryOperator<ChoicesModel> choicesModelOperator) {
    super(
        id,
        model,
        choicesModelOperator.apply(new ChoicesModel()),
        GenericEntityRendererToChoiceRenderer.of(SiteRenderer.get()));
  }

  public static class ChoicesModel extends LoadableDetachableModel<List<Site>> {

    private static final long serialVersionUID = 1L;

    @SpringBean private ISiteControllerService siteControllerService;

    public ChoicesModel() {
      Injector.get().inject(this);
    }

    @Override
    protected List<Site> load() {
      return siteControllerService.listByOrganisation(
          SekoyaSession.get().getOrganisationModel().getObject());
    }
  }
}
