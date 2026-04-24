package sekoya.front.common.validator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.util.validate.validators.AbstractUnicityValidator;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.role.service.controller.IRoleControllerService;

public class RoleTitleUnicityValidator extends AbstractUnicityValidator<Role, String> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IRoleControllerService roleControllerService;

  public RoleTitleUnicityValidator(IModel<Role> mainObjectModel) {
    super(mainObjectModel, "common.validator.role.title.unicity");
    Injector.get().inject(this);
  }

  @Override
  protected Role getByUniqueField(String value) {
    return roleControllerService.getByTitle(value);
  }
}
