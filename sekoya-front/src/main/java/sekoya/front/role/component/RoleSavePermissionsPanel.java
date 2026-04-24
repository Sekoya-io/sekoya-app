package sekoya.front.role.component;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.markup.html.form.CheckGroup;
import igloo.wicket.model.BindingModel;
import igloo.wicket.model.Models;
import igloo.wicket.model.ReadOnlyMapModel;
import java.util.List;
import java.util.Map;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.markup.repeater.map.MapItem;
import org.iglooproject.wicket.more.markup.repeater.map.MapView;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import sekoya.back.business.role.model.Role;
import sekoya.back.security.model.SekoyaPermissionCategoryEnum;
import sekoya.back.security.model.SekoyaPermissionUtils;
import sekoya.back.util.binding.Bindings;
import sekoya.front.common.renderer.PermissionNameRenderer;

public class RoleSavePermissionsPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public RoleSavePermissionsPanel(String id, IModel<Role> roleModel) {
    super(id);

    IModel<Map<SekoyaPermissionCategoryEnum, List<String>>> mapModel =
        LoadableDetachableModel.of(() -> SekoyaPermissionUtils.PERMISSIONS);

    add(
        new CheckGroup<>(
                "permissions",
                BindingModel.of(roleModel, Bindings.role().permissions()),
                Suppliers2.hashSet())
            .add(
                new MapView<>(
                    "categories",
                    ReadOnlyMapModel.of(mapModel, Models.serializableModelFactory())) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  protected void populateItem(
                      MapItem<SekoyaPermissionCategoryEnum, List<String>> item) {
                    IModel<SekoyaPermissionCategoryEnum> keyModel = item.getModel();
                    IModel<List<String>> valueModel = item.getValueModel();

                    item.add(
                        new CoreLabel(
                            "category",
                            EnumRenderer.get().render(keyModel.getObject(), getLocale())),
                        new CollectionView<>(
                            "permissions", valueModel, Models.serializableModelFactory()) {
                          private static final long serialVersionUID = 1L;

                          @Override
                          protected void populateItem(Item<String> item) {
                            item.add(
                                new Check<>("permission", item.getModel())
                                    .setLabel(
                                        PermissionNameRenderer.get().asModel(item.getModel())));
                          }
                        });
                  }
                })
            .setRenderBodyOnly(false));
  }
}
