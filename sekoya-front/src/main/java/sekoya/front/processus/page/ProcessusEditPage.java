package sekoya.front.processus.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.PROCESSUS_WRITE;

import igloo.wicket.model.Detachables;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.processus.model.Processus;
import sekoya.front.common.form.BindableModelForm;
import sekoya.front.processus.component.ProcessusSaveAleasPanel;
import sekoya.front.processus.component.ProcessusSaveDescriptionPanel;
import sekoya.front.processus.component.ProcessusSaveFooterPanel;
import sekoya.front.processus.component.ProcessusSaveHeaderPanel;
import sekoya.front.processus.model.ProcessusBindableModel;
import sekoya.front.processus.template.ProcessusTemplate;

public class ProcessusEditPage extends ProcessusTemplate {

  private static final long serialVersionUID = 1L;

  public static IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, Processus> MAPPER =
      LinkDescriptorBuilder.start()
          .model(Processus.class)
          .permission(PROCESSUS_WRITE)
          .map(CommonParameters.ID)
          .mandatory()
          .page(ProcessusEditPage.class);

  private final IModel<Processus> processusModel = new GenericEntityModel<>();

  public ProcessusEditPage(PageParameters parameters) {
    super(parameters);

    MAPPER
        .map(processusModel)
        .extractSafely(
            parameters, ProcessusListPage.linkDescriptor(), getString("common.error.unexpected"));

    ProcessusBindableModel processusBindableModel =
        new ProcessusBindableModel(new GenericEntityModel<>(), processusModel);

    add(new ProcessusSaveHeaderPanel("header", processusBindableModel));

    BindableModelForm<Processus> form = new BindableModelForm<>("form", processusBindableModel);
    add(form);

    form.add(
        new ProcessusSaveDescriptionPanel("description", processusBindableModel),
        new ProcessusSaveAleasPanel("aleas", processusBindableModel),
        new ProcessusSaveFooterPanel("footer", processusBindableModel));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(processusModel);
  }
}
