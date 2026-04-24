package sekoya.back.business.referencedata.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.springframework.stereotype.Service;
import sekoya.back.business.common.model.PostalCode;
import sekoya.back.business.referencedata.dao.ICityDao;
import sekoya.back.business.referencedata.model.City;

@Service
public class CityServiceImpl extends GenericEntityServiceImpl<Long, City> implements ICityService {

  private final ICityDao dao;

  public CityServiceImpl(ICityDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public City getByLabelAndPostalCode(String label, PostalCode postalCode) {
    return dao.getByLabelAndPostalCode(label, postalCode);
  }
}
