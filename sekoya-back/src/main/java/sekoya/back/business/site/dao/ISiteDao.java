package sekoya.back.business.site.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.site.model.Site;

public interface ISiteDao extends IGenericEntityDao<Long, Site> {

  Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom);
}
