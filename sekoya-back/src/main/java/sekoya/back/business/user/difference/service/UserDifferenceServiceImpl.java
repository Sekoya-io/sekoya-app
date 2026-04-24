package sekoya.back.business.user.difference.service;

import igloo.difference.AbstractConfiguredDifferenceServiceImpl;
import igloo.difference.model.DifferenceFields;
import sekoya.back.business.user.model.User;

public class UserDifferenceServiceImpl extends AbstractConfiguredDifferenceServiceImpl<User>
    implements IUserDifferenceService {

  public UserDifferenceServiceImpl(DifferenceFields fields) {
    super(fields);
  }
}
