package sekoya.front.common.map.model;

import java.io.Serializable;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import sekoya.back.business.common.model.atomic.Risque;
import sekoya.back.business.site.model.Site;

public record MapPoint(Long id, double lng, double lat, String color, String label)
    implements Serializable {

  public MapPoint {
    label = label == null ? "" : Jsoup.clean(label, Safelist.none());
  }

  public static Optional<MapPoint> of(Site site) {
    if (site == null || site.getLocalisation() == null) {
      return Optional.empty();
    }

    return Optional.of(
        new MapPoint(
            site.getId(),
            site.getLocalisation().getX(),
            site.getLocalisation().getY(),
            sitePointColor(site.getRisqueBrutRcp45Annee2055()),
            site.getNom()));
  }

  private static String sitePointColor(Risque risque) {
    return switch (risque) {
      case OPPORTUNITE -> "#cbd5e1";
      case FAIBLE -> "#10b981";
      case MODERE -> "#4ade80";
      case IMPORTANT -> "#facc15";
      case MAJEUR -> "#fb923c";
      case CRITIQUE -> "#dc2626";
    };
  }
}
