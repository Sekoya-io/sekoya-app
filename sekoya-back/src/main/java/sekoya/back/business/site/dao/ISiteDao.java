package sekoya.back.business.site.dao;

import java.util.List;
import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import sekoya.back.business.donneeclimatique.model.PointGeographique;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.site.model.Site;

public interface ISiteDao extends IGenericEntityDao<Long, Site> {

  PointGeographique getPointGeographique(Site site);

  Site getByOrganisationAndNomCaseInsensitive(Organisation organisation, String nom);

  List<Site> listByOrganisation(Organisation organisation);
}
