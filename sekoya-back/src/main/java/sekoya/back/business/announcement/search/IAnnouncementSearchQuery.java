package sekoya.back.business.announcement.search;

import org.iglooproject.jpa.more.search.query.IJpaSearchQuery;
import sekoya.back.business.announcement.model.Announcement;

public interface IAnnouncementSearchQuery
    extends IJpaSearchQuery<Announcement, AnnouncementSort, AnnouncementSearchQueryData> {}
