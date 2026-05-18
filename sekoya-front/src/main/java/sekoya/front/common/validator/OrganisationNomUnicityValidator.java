package sekoya.front.common.validator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.util.validate.validators.AbstractUnicityValidator;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.organisation.service.controller.IOrganisationControllerService;

public class OrganisationNomUnicityValidator
    extends AbstractUnicityValidator<Organisation, String> {

  private static final long serialVersionUID = 7351972908406394930L;

  @SpringBean private IOrganisationControllerService organisationControllerService;

  public OrganisationNomUnicityValidator(IModel<? extends Organisation> mainObjectModel) {
    super(mainObjectModel, "common.validator.organisation.nom.unicity");
    Injector.get().inject(this);
  }

  @Override
  protected Organisation getByUniqueField(String value) {
    return organisationControllerService.getByNomCaseInsensitive(value);
  }
}
