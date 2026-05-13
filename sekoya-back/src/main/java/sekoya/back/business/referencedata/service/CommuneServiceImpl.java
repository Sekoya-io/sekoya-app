package sekoya.back.business.referencedata.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
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
}
