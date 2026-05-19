package sekoya.front.site.renderer;

import igloo.bootstrap.common.BootstrapColor;
import java.util.Locale;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;
import sekoya.back.business.site.model.Site;

public abstract class SiteBootstrapRenderer extends BootstrapRenderer<Site> {

  private static final long serialVersionUID = 1L;

  private static final SiteBootstrapRenderer ENABLED =
      new SiteBootstrapRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        protected BootstrapRendererInformation doRender(Site value, Locale locale) {
          if (value == null) {
            return null;
          }
          if (value.isEnabled()) {
            return BootstrapRendererInformation.builder()
                .label(getString("business.common.enabled.true", locale))
                .icon("fa fa-fw fa-check")
                .color(BootstrapColor.SUCCESS)
                .build();
          } else {
            return BootstrapRendererInformation.builder()
                .label(getString("business.common.enabled.false", locale))
                .icon("fa fa-fw fa-times")
                .color(BootstrapColor.SECONDARY)
                .build();
          }
        }
      };

  public static final SiteBootstrapRenderer enabled() {
    return ENABLED;
  }

  private SiteBootstrapRenderer() {}
}
