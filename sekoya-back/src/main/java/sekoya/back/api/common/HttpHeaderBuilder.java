package sekoya.back.api.common;

import com.google.common.collect.Maps;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.spring.util.StringUtils;

public final class HttpHeaderBuilder {

  private final Map<String, String> headers = Maps.newHashMap();

  public static final String AUTHORIZATION = "Authorization";
  public static final String BEARER = "Bearer ";
  public static final String BASIC = "Basic ";

  public static final String CONN_DIRECTIVE = "Connection";
  public static final String CONN_CLOSE = "Close";
  public static final String CONN_KEEP_ALIVE = "Keep-Alive";
  public static final String ACCEPT = "Accept";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String USER_AGENT = "User-Agent";

  public static final String MULTIPART_FORM_DATA = "multipart/form-data";
  public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

  public static final HttpHeaderBuilder start() {
    return new HttpHeaderBuilder();
  }

  public String[] build() {
    return headers.entrySet().stream()
        .flatMap(e -> List.of(e.getKey(), e.getValue()).stream())
        .toArray(String[]::new);
  }

  public HttpHeaderBuilder connDirectiveClose() {
    headers.put(CONN_DIRECTIVE, CONN_CLOSE);
    return this;
  }

  public HttpHeaderBuilder contentTypeJson() {
    return this.contentType(MediaType.APPLICATION_JSON.mime());
  }

  public HttpHeaderBuilder contentTypeMultipartFormData() {
    return this.contentType(MULTIPART_FORM_DATA);
  }

  public HttpHeaderBuilder contentType(String contentType) {
    if (contentType != null) {
      headers.put(CONTENT_TYPE, contentType);
    }
    return this;
  }

  public HttpHeaderBuilder acceptJson() {
    return this.accept(MediaType.APPLICATION_JSON.mime());
  }

  public HttpHeaderBuilder accept(String accept) {
    if (accept != null) {
      headers.put(ACCEPT, accept);
    }
    return this;
  }

  public HttpHeaderBuilder authorization(String authorization) {
    if (authorization != null) {
      headers.put(AUTHORIZATION, authorization);
    }
    return this;
  }

  public HttpHeaderBuilder authorizationBearer(String token) {
    if (token != null) {
      authorization(BEARER + token);
    }
    return this;
  }

  public HttpHeaderBuilder authorizationBasic(String username, String password) {
    if (username != null && password != null) {
      authorizationBasic(username + ":" + password);
    }
    return this;
  }

  public HttpHeaderBuilder authorizationBasic(String notEncodedValue) {
    if (notEncodedValue != null) {
      String encoded =
          Base64.getEncoder().encodeToString(notEncodedValue.getBytes(StandardCharsets.UTF_8));
      authorization(BASIC + encoded);
    }
    return this;
  }

  public HttpHeaderBuilder userAgent(String userAgent) {
    if (userAgent != null) {
      headers.put(USER_AGENT, userAgent);
    }
    return this;
  }

  public HttpHeaderBuilder header(String name, String value) {
    if (StringUtils.hasText(name) && StringUtils.hasText(value)) {
      headers.put(name, value);
    }
    return this;
  }
}
