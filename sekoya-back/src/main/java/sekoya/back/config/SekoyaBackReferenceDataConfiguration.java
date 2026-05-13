package sekoya.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sekoya.back.business.referencedata.search.CommuneSearchQueryImpl;
import sekoya.back.business.referencedata.search.DepartementSearchQueryImpl;
import sekoya.back.business.referencedata.search.ICommuneSearchQuery;
import sekoya.back.business.referencedata.search.IDepartementSearchQuery;
import sekoya.back.business.referencedata.search.IRegionSearchQuery;
import sekoya.back.business.referencedata.search.RegionSearchQueryImpl;

@Configuration
public class SekoyaBackReferenceDataConfiguration {

  @Configuration
  public class SearchQuery {
    @Bean
    public ICommuneSearchQuery communeSearchQuery() {
      return new CommuneSearchQueryImpl();
    }

    @Bean
    public IDepartementSearchQuery departementSearchQuery() {
      return new DepartementSearchQueryImpl();
    }

    @Bean
    public IRegionSearchQuery regionSearchQuery() {
      return new RegionSearchQueryImpl();
    }
  }
}
