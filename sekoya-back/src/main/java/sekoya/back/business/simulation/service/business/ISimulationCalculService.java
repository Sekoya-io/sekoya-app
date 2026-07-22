package sekoya.back.business.simulation.service.business;

import java.util.SortedSet;
import org.javatuples.Pair;
import org.springframework.transaction.annotation.Transactional;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationParametresDto;
import sekoya.back.business.site.model.Site;

@Transactional(readOnly = true)
public interface ISimulationCalculService {

  Risque getSiteRisqueBrut(Site site, SimulationParametresDto simulationParametresDto);

  Risque getProcessusRisqueBrut(
      Processus processus, SimulationParametresDto simulationParametresDto);

  Evolution getAleaEvolution(Alea alea, SimulationParametresDto simulationParametresDto);

  Risque getAleaRisqueBrut(Alea alea, SimulationParametresDto simulationParametresDto);

  SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographiqueBySite(
      Site site, SimulationParametresDto simulationParametresDto);
}
