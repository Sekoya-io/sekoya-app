package sekoya.back.api.geocodage.service;

import sekoya.back.api.common.RestClientCommunicationException;
import sekoya.back.api.geocodage.bean.GeocodageResponseBean;

public interface IGeocodageRestClientService {

  GeocodageResponseBean getSearch(String query) throws RestClientCommunicationException;
  ;
}
