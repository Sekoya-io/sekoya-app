package sekoya.front.user.util;

import org.apache.wicket.Page;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.util.LinkDescriptors;
import sekoya.back.business.user.model.User;
import sekoya.back.business.user.model.atomic.UserType;
import sekoya.back.util.binding.Bindings;
import sekoya.front.user.page.UserAdministrateurFonctionnelDetailPage;
import sekoya.front.user.page.UserOrganisationDetailPage;

public final class UserLinkDescriptors {

  public static ITwoParameterLinkDescriptorMapper<? extends IPageLinkDescriptor, User, Page>
      detailMapper(User user) {
    return detailMapper(Bindings.user().type().apply(user));
  }

  public static ITwoParameterLinkDescriptorMapper<? extends IPageLinkDescriptor, User, Page>
      detailMapper(UserType userType) {
    if (userType == null) {
      return LinkDescriptors.invalidTwoParameterMapper();
    }

    return switch (userType) {
      case ORGANISATION -> UserOrganisationDetailPage.MAPPER;
      case ADMINISTRATEUR_FONCTIONNEL -> UserAdministrateurFonctionnelDetailPage.MAPPER;
      default -> LinkDescriptors.invalidTwoParameterMapper();
    };
  }

  private UserLinkDescriptors() {}
}
