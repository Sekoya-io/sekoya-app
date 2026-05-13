package sekoya.back.business.referencedata.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.referencedata.model.Commune;

@Repository
public class CommuneDaoImpl extends GenericEntityDaoImpl<Long, Commune> implements ICommuneDao {}
