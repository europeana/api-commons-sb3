package eu.europeana.api.commons_sb3.http;

import java.util.List;
import java.util.Map;
import org.apache.hc.core5.http.HttpRequest;

/**
 * Abstract base class for managing HTTP connections and requests.
 * Provides methods to add headers to HTTP requests, both single-value and multi-value headers.
 *
 * @author Sergiu Gordea
 * @since 09 April 2026
 */
public class AbstractHttpConnection {

  /**
   * add single valued, or serialized multivalue headers to the request
   * @param request the request
   * @param headers headers to append
   */
  protected void addHeaders(HttpRequest request, Map<String, String> headers) {
    if (headers == null) {
      return;
    }
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      request.addHeader(entry.getKey(), entry.getValue());
    }
  }
  
  /**
   * Method used to append headers with multiple values to the provided request
   * 
   * @param request the request
   * @param headers headers to append
   */
  public void addMultiValueHeaders(HttpRequest request,
      Map<String, List<String>> headers) {
    if (headers == null) {
      return;
    }
    for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
      for (String headerValue : entry.getValue()) {
        request.addHeader(entry.getKey(), headerValue);
      }
    }
  }

  
}
