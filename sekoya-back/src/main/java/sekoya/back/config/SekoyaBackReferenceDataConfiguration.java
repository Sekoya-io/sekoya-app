package sekoya.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sekoya.back.business.referencedata.search.CitySearchQueryImpl;
import sekoya.back.business.referencedata.search.ICitySearchQuery;

@Configuration
public class SekoyaBackReferenceDataConfiguration {

  @Configuration
  public class SearchQuery {
    @Bean
    public ICitySearchQuery citySearchQuery() {
      return new CitySearchQueryImpl();
    }
  }
}
