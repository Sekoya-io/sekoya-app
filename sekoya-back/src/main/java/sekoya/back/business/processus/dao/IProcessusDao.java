package sekoya.back.business.processus.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.processus.model.atomic.ProcessusType;
import sekoya.back.business.site.model.Site;

public interface IProcessusDao extends IGenericEntityDao<Long, Processus> {

  Processus getBySiteAndType(Site site, ProcessusType type);
}
