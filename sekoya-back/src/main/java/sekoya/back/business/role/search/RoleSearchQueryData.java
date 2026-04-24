package sekoya.back.business.role.search;

import org.bindgen.Bindable;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.user.model.User;

@Bindable
public class RoleSearchQueryData implements ISearchQueryData<Role> {

  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
