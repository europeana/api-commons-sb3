package eu.europeana.api.commons_sb3.http;

import java.util.List;
import java.util.Map;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

public class AbstractHttpConnection {

  protected <T extends HttpUriRequestBase> void addHeaders(T request, Map<String, String> headers) {
    if (headers == null) {
      return;
    }
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      request.setHeader(entry.getKey(), entry.getValue());
    }
  }
  
  /**
   * Method used to append headers with multiple values to
   * 
   * @param <T> must extend HttpUriRequestBase
   * @param request
   * @param headers
   */
  public <T extends HttpUriRequestBase> void addMultiValueHeaders(T request,
      Map<String, List<String>> headers) {
    if (headers == null) {
      return;
    }
    for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
      request.setHeader(entry.getKey(), entry.getValue());
    }
  }

  
}
