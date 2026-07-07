package sekoya.back.business.simulation.service.controller;

import java.util.SortedSet;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.simulation.service.business.ISimulationCalculService;
import sekoya.back.business.site.model.Site;

@Service
public class SimulationCalculControllerServiceImpl implements ISimulationCalculControllerService {

  private final ISimulationCalculService simulationCalculService;

  public SimulationCalculControllerServiceImpl(ISimulationCalculService simulationCalculService) {
    this.simulationCalculService = simulationCalculService;
  }

  @Override
  public Risque getSiteRisqueBrut(Site site, SimulationSearchDto simulationSearchDto) {
    return simulationCalculService.getSiteRisqueBrut(site, simulationSearchDto);
  }

  @Override
  public SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographiqueBySite(
      Site site, SimulationSearchDto simulationSearchDto) {
    return simulationCalculService.listAleaRisqueGeographiqueBySite(site, simulationSearchDto);
  }
}
