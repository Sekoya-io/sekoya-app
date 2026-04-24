package sekoya.back.business.history.dao;

import org.iglooproject.jpa.more.business.history.dao.IGenericHistoryLogDao;
import sekoya.back.business.history.model.HistoryLog;
import sekoya.back.business.history.model.atomic.HistoryLogEventType;

public interface IHistoryLogDao extends IGenericHistoryLogDao<HistoryLog, HistoryLogEventType> {}
