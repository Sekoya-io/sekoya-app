package sekoya.back.business.history.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.more.business.history.service.IGenericHistoryLogService;
import sekoya.back.business.history.model.HistoryDifference;
import sekoya.back.business.history.model.HistoryLog;
import sekoya.back.business.history.model.atomic.HistoryLogEventType;
import sekoya.back.business.history.model.bean.HistoryLogAdditionalInformationBean;

public interface IHistoryLogService
    extends IGenericHistoryLogService<
            HistoryLog,
            HistoryLogEventType,
            HistoryDifference,
            HistoryLogAdditionalInformationBean>,
        IGenericEntityService<Long, HistoryLog> {}
