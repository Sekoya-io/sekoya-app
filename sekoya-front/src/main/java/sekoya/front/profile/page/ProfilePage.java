package sekoya.front.profile.page;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Detachables;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import sekoya.back.business.user.model.User;
import sekoya.back.util.binding.Bindings;
import sekoya.front.SekoyaSession;
import sekoya.front.profile.component.ProfileDescriptionPanel;
import sekoya.front.profile.template.ProfileTemplate;

public class ProfilePage extends ProfileTemplate {

  private static final long serialVersionUID = -8757939680257114559L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(ProfilePage.class);
  }

  protected final IModel<User> userModel = SekoyaSession.get().getUserModel();

  public ProfilePage(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(BindingModel.of(userModel, Bindings.user().fullName())));

    add(new CoreLabel("title", BindingModel.of(userModel, Bindings.user().fullName())));

    add(new ProfileDescriptionPanel("description", userModel));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(userModel);
  }
}
