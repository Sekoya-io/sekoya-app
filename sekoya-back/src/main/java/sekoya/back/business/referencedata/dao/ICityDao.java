package sekoya.back.business.referencedata.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.common.model.PostalCode;
import sekoya.back.business.referencedata.model.City;

public interface ICityDao extends IGenericEntityDao<Long, City> {

  City getByLabelAndPostalCode(String label, PostalCode postalCode);
}
