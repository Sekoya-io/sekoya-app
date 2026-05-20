package sekoya.back.business.referencedata.service.controller;

import org.springframework.stereotype.Service;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.service.business.ICommuneService;

@Service
public class CommuneControllerServiceImpl implements ICommuneControllerService {

  private final ICommuneService communeService;

  public CommuneControllerServiceImpl(ICommuneService communeService) {
    this.communeService = communeService;
  }

  @Override
  public Commune getByCodeInsee(String codeInsee) {
    return communeService.getByCodeInsee(codeInsee);
  }
}
