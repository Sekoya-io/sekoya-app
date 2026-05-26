package sekoya.front.processus.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_PROCESSUS_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_READ;

import igloo.wicket.condition.Condition;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.site.model.Site;
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
    return MAPPER.ignoreParameter1();
  }

  public static final IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, Site> MAPPER =
      LinkDescriptorBuilder.start()
          .model(Site.class)
          .permission(SITE_READ)
          .map("site")
          .mandatory()
          .validator(Condition.permission(GLOBAL_PROCESSUS_WRITE))
          .page(ProcessusAddPage.class);

  public ProcessusAddPage(PageParameters parameters) {
    super(parameters);

    ProcessusBindableModel processusBindableModel =
        new ProcessusBindableModel(GenericEntityModel.of(new Processus()));

    MAPPER
        .map(processusBindableModel.getSiteModel())
        .extractSafely(
            parameters, ProcessusListPage.linkDescriptor(), getString("common.error.unexpected"));

    processusBindableModel.getObject().setSite(processusBindableModel.getSiteModel().getObject());

    processusBindableModel.readAll();

    add(new ProcessusSaveHeaderPanel("header", processusBindableModel));

    BindableModelForm<Processus> form = new BindableModelForm<>("form", processusBindableModel);
    add(form);

    form.add(
        new ProcessusSaveDescriptionPanel("description", processusBindableModel),
        new ProcessusSaveAleasPanel("aleas", processusBindableModel),
        new ProcessusSaveFooterPanel("footer", processusBindableModel));
  }
}
