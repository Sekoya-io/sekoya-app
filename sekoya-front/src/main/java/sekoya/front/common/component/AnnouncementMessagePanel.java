package sekoya.front.common.component;

import igloo.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import sekoya.back.business.announcement.model.Announcement;
import sekoya.front.announcement.renderer.AnnouncementRenderer;
import sekoya.front.common.commonmark.component.CommonMarkLabel;

public class AnnouncementMessagePanel extends GenericPanel<Announcement> {

  private static final long serialVersionUID = 7392973418033689115L;

  public AnnouncementMessagePanel(String id, IModel<Announcement> announcementModel) {
    super(id, announcementModel);
    setOutputMarkupId(true);

    add(new CommonMarkLabel("content", AnnouncementRenderer.content().asModel(announcementModel)));
  }
}
