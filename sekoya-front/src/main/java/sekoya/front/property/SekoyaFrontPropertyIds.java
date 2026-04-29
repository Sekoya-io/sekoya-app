package sekoya.front.property;

import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;

public final class SekoyaFrontPropertyIds extends AbstractPropertyIds {

  /*
   * Mutable Properties
   */

  /*
   * Immutable Properties
   */

  public static final ImmutablePropertyId<Integer> PORTFOLIO_ITEMS_PER_PAGE =
      immutable("portfolio.itemsPerPage");
  public static final ImmutablePropertyId<Integer> PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION =
      immutable("portfolio.itemsPerPage.description");

  public static final ImmutablePropertyId<String> MAINTENANCE_URL = immutable("maintenance.url");
}
