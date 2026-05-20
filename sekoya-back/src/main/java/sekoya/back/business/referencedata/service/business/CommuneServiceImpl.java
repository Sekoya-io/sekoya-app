package sekoya.back.business.referencedata.service.business;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.stereotype.Service;
import sekoya.back.business.referencedata.dao.ICommuneDao;
import sekoya.back.business.referencedata.model.Commune;

@Service
public class CommuneServiceImpl extends GenericEntityServiceImpl<Long, Commune>
    implements ICommuneService {

  private final ICommuneDao dao;

  public CommuneServiceImpl(ICommuneDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public Commune getByCodeInsee(String codeInsee) {
    if (!StringUtils.hasText(codeInsee)) {
      return null;
    }
    return dao.getByCodeInsee(codeInsee);
  }
}
