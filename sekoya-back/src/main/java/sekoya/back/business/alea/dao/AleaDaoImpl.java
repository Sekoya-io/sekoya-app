package sekoya.back.business.alea.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.alea.model.Alea;

@Repository
public class AleaDaoImpl extends GenericEntityDaoImpl<Long, Alea> implements IAleaDao {}
