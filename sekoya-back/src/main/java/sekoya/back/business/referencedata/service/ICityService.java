package sekoya.back.business.referencedata.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import sekoya.back.business.common.model.PostalCode;
import sekoya.back.business.referencedata.model.City;

public interface ICityService extends IGenericEntityService<Long, City> {

  City getByLabelAndPostalCode(String label, PostalCode postalCode);
}
