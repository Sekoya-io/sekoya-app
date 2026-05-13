package sekoya.front.common.validator;

import org.apache.commons.validator.routines.RegexValidator;

public class CodePostalValidator extends RegexValidator {

  private static final long serialVersionUID = 1L;

  private static final CodePostalValidator INSTANCE = new CodePostalValidator();

  private static final String REGEX = "^\\d{5}$";

  public static CodePostalValidator getInstance() {
    return INSTANCE;
  }

  public CodePostalValidator() {
    super(REGEX, false);
  }
}
