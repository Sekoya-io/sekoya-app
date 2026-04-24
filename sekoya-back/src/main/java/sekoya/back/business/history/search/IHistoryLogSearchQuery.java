package sekoya.back.business.history.search;

import org.iglooproject.jpa.more.search.query.IJpaSearchQuery;
import sekoya.back.business.history.model.HistoryLog;

public interface IHistoryLogSearchQuery
    extends IJpaSearchQuery<HistoryLog, HistoryLogSort, HistoryLogSearchQueryData> {}
