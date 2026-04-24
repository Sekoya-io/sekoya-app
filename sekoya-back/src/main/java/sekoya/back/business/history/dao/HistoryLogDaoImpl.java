package sekoya.back.business.history.dao;

import org.iglooproject.jpa.more.business.history.dao.AbstractHistoryLogDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.history.model.HistoryLog;
import sekoya.back.business.history.model.atomic.HistoryLogEventType;

@Repository
public class HistoryLogDaoImpl extends AbstractHistoryLogDaoImpl<HistoryLog, HistoryLogEventType>
    implements IHistoryLogDao {}
