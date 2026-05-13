package sekoya.back.business.organisation.dao;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import sekoya.back.business.organisation.model.Organisation;

@Repository
public class OrganisationDaoImpl extends GenericEntityDaoImpl<Long, Organisation>
    implements IOrganisationDao {}
