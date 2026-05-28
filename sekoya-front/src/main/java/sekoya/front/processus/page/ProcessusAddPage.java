package sekoya.front.processus.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.processus.model.Processus;
import sekoya.front.common.form.BindableModelForm;
import sekoya.front.processus.component.ProcessusSaveAleasPanel;
import sekoya.front.processus.component.ProcessusSaveDescriptionPanel;
import sekoya.front.processus.component.ProcessusSaveFooterPanel;
import sekoya.front.processus.component.ProcessusSaveHeaderPanel;
import sekoya.front.processus.model.ProcessusBindableModel;
import sekoya.front.processus.template.ProcessusTemplate;

public class ProcessusAddPage extends ProcessusTemplate {

  private static final long serialVersionUID = 1L;

  public static IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start().page(ProcessusAddPage.class);
  }

  public ProcessusAddPage(PageParameters parameters) {
    super(parameters);

    ProcessusBindableModel processusBindableModel =
        new ProcessusBindableModel(
            new GenericEntityModel<>(), GenericEntityModel.of(new Processus()));
    add(new ProcessusSaveHeaderPanel("header", processusBindableModel));

    BindableModelForm<Processus> form = new BindableModelForm<>("form", processusBindableModel);
    add(form);

    form.add(
        new ProcessusSaveDescriptionPanel("description", processusBindableModel),
        new ProcessusSaveAleasPanel("aleas", processusBindableModel),
        new ProcessusSaveFooterPanel("footer", processusBindableModel));
  }
}
