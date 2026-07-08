package sekoya.back.business.simulation.service.business;

import java.util.SortedSet;
import org.javatuples.Pair;
import org.springframework.transaction.annotation.Transactional;
import sekoya.back.business.alea.model.Alea;
import sekoya.back.business.alea.model.atomic.AleaType;
import sekoya.back.business.common.model.atomic.Evolution;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.processus.model.Processus;
import sekoya.back.business.simulation.dto.SimulationSearchDto;
import sekoya.back.business.site.model.Site;

@Transactional(readOnly = true)
public interface ISimulationCalculService {

  Risque getSiteRisqueBrut(Site site, SimulationSearchDto simulationSearchDto);

  Risque getProcessusRisqueBrut(Processus processus, SimulationSearchDto simulationSearchDto);

  Evolution getAleaEvolution(Alea alea, SimulationSearchDto simulationSearchDto);

  Risque getAleaRisqueBrut(Alea alea, SimulationSearchDto simulationSearchDto);

  SortedSet<Pair<AleaType, Risque>> listAleaRisqueGeographiqueBySite(
      Site site, SimulationSearchDto simulationSearchDto);
}
