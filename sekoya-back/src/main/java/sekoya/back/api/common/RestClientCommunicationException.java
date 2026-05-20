package sekoya.back.api.common;

import org.iglooproject.jpa.exception.ServiceException;

public class RestClientCommunicationException extends ServiceException {

  private static final long serialVersionUID = 1L;

  public RestClientCommunicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public RestClientCommunicationException(String message) {
    super(message);
  }
}
