package sekoya.front.processus.page;

import static sekoya.back.security.model.SekoyaPermissionConstants.GLOBAL_PROCESSUS_WRITE;
import static sekoya.back.security.model.SekoyaPermissionConstants.SITE_READ;

import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
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

public class ProcessusSiteAddPage extends ProcessusTemplate {

  private static final long serialVersionUID = 1L;

  public static final IOneParameterLinkDescriptorMapper<IPageLinkDescriptor, Site> MAPPER =
      LinkDescriptorBuilder.start()
          .model(Site.class)
          .permission(SITE_READ)
          .map(CommonParameters.ID)
          .mandatory()
          .validator(Condition.permission(GLOBAL_PROCESSUS_WRITE))
          .page(ProcessusSiteAddPage.class);

  private final IModel<Site> siteModel = new GenericEntityModel<>();

  public ProcessusSiteAddPage(PageParameters parameters) {
    super(parameters);

    MAPPER
        .map(siteModel)
        .extractSafely(
            parameters, ProcessusListPage.linkDescriptor(), getString("common.error.unexpected"));

    Processus processus = new Processus();
    processus.setSite(siteModel.getObject());

    ProcessusBindableModel processusBindableModel =
        new ProcessusBindableModel(siteModel, GenericEntityModel.of(processus));

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
    Detachables.detach(siteModel);
  }
}
