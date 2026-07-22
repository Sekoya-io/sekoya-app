package sekoya.back.business.simulation.service.controller;

import java.util.SortedSet;
import org.javatuples.Pair;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.simulation.dto.SimulationParametresDto;
import sekoya.back.business.site.model.Site;

public interface ISimulationCalculControllerService {

  Risque getSiteRisqueBrut(Site site, SimulationParametresDto simulationParametresDto);

  SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographiqueBySite(
      Site site, SimulationParametresDto simulationParametresDto);
}
