package sekoya.back.business.referencedata.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.common.model.PostalCode;
import sekoya.back.business.referencedata.model.City;
import sekoya.back.business.referencedata.model.QCity;

@Repository
public class CityDaoImpl extends GenericEntityDaoImpl<Long, City> implements ICityDao {

  @Override
  public City getByLabelAndPostalCode(String label, PostalCode postalCode) {
    return new JPAQuery<>(getEntityManager())
        .select(QCity.city)
        .from(QCity.city)
        .where(QCity.city.label.eq(label))
        .where(QCity.city.postalCode.eq(postalCode))
        .fetchOne();
  }
}
