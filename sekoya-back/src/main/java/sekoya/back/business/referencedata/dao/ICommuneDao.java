package sekoya.back.business.referencedata.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.referencedata.model.Commune;

public interface ICommuneDao extends IGenericEntityDao<Long, Commune> {

  Commune getByCodeInsee(String codeInsee);
}
