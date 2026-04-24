package sekoya.front.common.template.theme;

import igloo.wicket.condition.Condition;
import java.util.List;
import java.util.function.Supplier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import sekoya.front.common.component.ApplicationEnvironmentPanel;
import sekoya.front.common.template.MainTemplate;

public enum SekoyaApplicationTheme {
  BASIC {
    @Override
    public String getMarkupVariation() {
      return "basic";
    }

    @Override
    public void renderHead(IHeaderResponse response) {
      response.render(
          CssHeaderItem.forReference(
              sekoya.front.common.template.resources.styles.application.application.applicationbasic
                  .StylesScssResourceReference.get()));
    }

    @Override
    public void specificContent(
        MainTemplate mainTemplate,
        Supplier<List<NavigationMenuItem>> mainNavSupplier,
        Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
        Supplier<Class<? extends WebPage>> secondMenuPageSupplier) {
      mainTemplate.add(
          new sekoya.front.common.template.theme.basic.NavbarPanel(
              "navbar", mainNavSupplier, firstMenuPageSupplier, secondMenuPageSupplier),
          new ApplicationEnvironmentPanel("environment"),
          new sekoya.front.common.template.theme.basic.FooterPanel("footer"));
    }

    @Override
    public SekoyaApplicationTheme next() {
      return SekoyaApplicationTheme.ADVANCED;
    }
  },
  ADVANCED {
    @Override
    public String getMarkupVariation() {
      return "advanced";
    }

    @Override
    public void renderHead(IHeaderResponse response) {
      response.render(
          CssHeaderItem.forReference(
              sekoya.front.common.template.resources.styles.application.application
                  .applicationadvanced.StylesScssResourceReference.get()));
    }

    @Override
    public void specificContent(
        MainTemplate mainTemplate,
        Supplier<List<NavigationMenuItem>> mainNavSupplier,
        Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
        Supplier<Class<? extends WebPage>> secondMenuPageSupplier) {
      mainTemplate.add(
          new sekoya.front.common.template.theme.advanced.NavbarPanel(
              "navbar", firstMenuPageSupplier),
          new sekoya.front.common.template.theme.advanced.SidebarPanel(
              "sidebar", mainNavSupplier, firstMenuPageSupplier, secondMenuPageSupplier));
    }

    @Override
    public SekoyaApplicationTheme next() {
      return SekoyaApplicationTheme.BASIC;
    }
  };

  public abstract String getMarkupVariation();

  public abstract void renderHead(IHeaderResponse response);

  public abstract void specificContent(
      MainTemplate mainTemplate,
      Supplier<List<NavigationMenuItem>> mainNavSupplier,
      Supplier<Class<? extends WebPage>> firstMenuPageSupplier,
      Supplier<Class<? extends WebPage>> secondMenuPageSupplier);

  public abstract SekoyaApplicationTheme next();

  public Condition isEqual(SekoyaApplicationTheme applicationTheme) {
    if (applicationTheme == null) {
      return Condition.alwaysFalse();
    }
    return Condition.isEqual(Model.of(this), Model.of(applicationTheme));
  }

  public Condition isBasic() {
    return isEqual(SekoyaApplicationTheme.BASIC);
  }

  public Condition isAdvanced() {
    return isEqual(SekoyaApplicationTheme.ADVANCED);
  }
}
