package sekoya.back.business.referencedata.service.controller;

import sekoya.back.business.referencedata.model.Commune;

public interface ICommuneControllerService {

  Commune getByCodeInsee(String codeInsee);
}
