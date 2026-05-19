package sekoya.front.common.validator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.util.validate.validators.AbstractUnicityValidator;
import sekoya.back.business.site.model.Site;
import sekoya.back.business.site.service.controller.ISiteControllerService;
import sekoya.front.SekoyaSession;

public class SiteNomUnicityValidator extends AbstractUnicityValidator<Site, String> {

  private static final long serialVersionUID = 1L;

  @SpringBean private ISiteControllerService siteControllerService;

  public SiteNomUnicityValidator(IModel<? extends Site> mainObjectModel) {
    super(mainObjectModel, "common.validator.site.nom.unicity");
    Injector.get().inject(this);
  }

  @Override
  protected Site getByUniqueField(String value) {
    return siteControllerService.getByOrganisationAndNomCaseInsensitive(
        SekoyaSession.get().getOrganisationModel().getObject(), value);
  }
}
