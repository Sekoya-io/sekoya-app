package sekoya.front.security.password.page;

import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.user.model.User;
import sekoya.front.SekoyaApplication;
import sekoya.front.SekoyaSession;
import sekoya.front.security.password.component.SecurityPasswordCreationContentPanel;
import sekoya.front.security.password.template.SecurityPasswordTemplate;

public class SecurityPasswordCreationPage extends SecurityPasswordTemplate {

  private static final long serialVersionUID = 3082299089180143270L;

  public static final ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, User, String> MAPPER =
      LinkDescriptorBuilder.start()
          .model(User.class)
          .model(String.class)
          .pickFirst()
          .map(CommonParameters.ID)
          .mandatory()
          .pickSecond()
          .map(CommonParameters.TOKEN)
          .mandatory()
          .page(SecurityPasswordCreationPage.class);

  private final IModel<User> userModel = new GenericEntityModel<>();

  public SecurityPasswordCreationPage(PageParameters parameters) {
    super(parameters);

    addHeadPageTitlePrependedElement(
        new BreadCrumbElement(new ResourceModel("security.password.creation.title")));

    final IModel<String> tokenModel = Model.of("");

    MAPPER
        .map(userModel, tokenModel)
        .extractSafely(
            parameters,
            SekoyaApplication.get().getHomePageLinkDescriptor(),
            getString("common.error.unexpected"));

    if (!tokenModel
        .getObject()
        .equals(userModel.getObject().getPasswordRecoveryRequest().getToken())) {
      SekoyaSession.get().error(getString("security.password.creation.wrongToken"));
      throw SekoyaApplication.get().getHomePageLinkDescriptor().newRestartResponseException();
    }
  }

  @Override
  protected IModel<String> getTitleModel() {
    return new ResourceModel("security.password.creation.title");
  }

  @Override
  protected Component getContentComponent(String wicketId) {
    return new SecurityPasswordCreationContentPanel(wicketId, userModel);
  }

  @Override
  public Condition keepSignedIn() {
    return Condition.alwaysFalse();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(userModel);
  }
}
