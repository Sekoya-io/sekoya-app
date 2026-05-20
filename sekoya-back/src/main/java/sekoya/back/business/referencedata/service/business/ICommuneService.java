package sekoya.back.business.referencedata.service.business;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import sekoya.back.business.referencedata.model.Commune;

public interface ICommuneService extends IGenericEntityService<Long, Commune> {

  Commune getByCodeInsee(String codeInsee);
}
