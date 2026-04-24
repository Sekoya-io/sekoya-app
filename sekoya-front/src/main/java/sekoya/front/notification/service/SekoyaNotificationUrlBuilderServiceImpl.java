package sekoya.front.notification.service;

import java.util.concurrent.Callable;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.notification.service.AbstractNotificationUrlBuilderServiceImpl;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import sekoya.back.business.notification.service.ISekoyaNotificationUrlBuilderService;
import sekoya.back.business.user.model.User;
import sekoya.front.user.util.UserLinkDescriptors;

/**
 * This service is used to generate the URL used in the text version of the notification emails.
 *
 * <p>It shouldn't be used for other purposes.
 */
public class SekoyaNotificationUrlBuilderServiceImpl
    extends AbstractNotificationUrlBuilderServiceImpl
    implements ISekoyaNotificationUrlBuilderService {

  @Autowired
  public SekoyaNotificationUrlBuilderServiceImpl(IWicketContextProvider contextProvider) {
    super(contextProvider);
  }

  @Override
  public String getUserDescriptionUrl(final User user) {
    Callable<IPageLinkGenerator> pageLinkGeneratorTask =
        () ->
            UserLinkDescriptors.detailMapper(user)
                .ignoreParameter2()
                .map(GenericEntityModel.of(user));

    return buildUrl(pageLinkGeneratorTask);
  }
}
