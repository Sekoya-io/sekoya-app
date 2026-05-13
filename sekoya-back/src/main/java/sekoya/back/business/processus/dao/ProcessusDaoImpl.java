package sekoya.back.business.processus.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.processus.model.Processus;

@Repository
public class ProcessusDaoImpl extends GenericEntityDaoImpl<Long, Processus>
    implements IProcessusDao {}
