package eu.europeana.api.commons_sb3.http;

import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class HttpResponseHandlerAsync implements FutureCallback<SimpleHttpResponse> {

  private String response;
  private int status;
  private String locationHeader; // fetched only if status is 301

  @Override
  public void completed(SimpleHttpResponse simpleHttpResponse) {
    this.status = simpleHttpResponse.getCode();
    this.response = simpleHttpResponse.getBodyText();
    try {
      this.locationHeader = simpleHttpResponse.getHeader(HttpHeaders.LOCATION).getValue();
    } catch (ProtocolException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void failed(Exception e) {

  }

  @Override
  public void cancelled() {

  }

  public String getResponse() {
    return response;
  }

  public int getStatus() {
    return status;
  }

  public String getLocationHeader() {
    return locationHeader;
  }
}
