package sekoya.back.business.site.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.site.model.Site;

@Repository
public class SiteDaoImpl extends GenericEntityDaoImpl<Long, Site> implements ISiteDao {}
