package eu.europeana.api.commons_sb3.http;

import java.io.IOException;
import java.util.Map;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.routing.RoutingSupport;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;

/**
 * The class encapsulating simple HTTP access. Handles the authentication for the clients as well
 * via AuthenticationHandler
 *
 * @author Srishti Singh
 * @since 26 November 2024
 */
public class HttpConnection extends AbstractHttpConnection{

  private final CloseableHttpClient httpClient;

  /**
   * Creates a HttpConnection without redirect strategy
   */
  public HttpConnection() {
    this(false);
  }

  /**
   * By default - DefaultRedirectStrategy is initialised if nothing is set. To create a connection
   * without following redirects set 'redirectHandlingDisabled' to false
   * 
   * @param withRedirect
   */
  public HttpConnection(boolean withRedirect) {
    if (withRedirect) {
      RequestConfig requestConfig = RequestConfig.custom().build();

      this.httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
          .setRedirectStrategy(new DefaultRedirectStrategy()).build();
    } else {
      HttpClientBuilder clientBuilder = HttpClientBuilder.create();
      clientBuilder.disableRedirectHandling();
      this.httpClient = clientBuilder.build();
    }
  }

  /**
   * Creates the client with the desired connection pool settings This client will follow the
   * redirects by default
   * 
   * @param cm connectionPool param
   */
  public HttpConnection(PoolingHttpClientConnectionManager cm) {
    RequestConfig requestConfig = RequestConfig.custom().build();

    this.httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
        .setConnectionManager(cm).setRedirectStrategy(new DefaultRedirectStrategy()).build();
  }

  /**
   * This method makes GET request for given URL.
   * 
   * @param url
   * @param headers map of header name and value that needs to be added in the Url
   * @param auth Authentication handler for the request
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException if the request execution fails
   */
  public CloseableHttpResponse get(String url, Map<String, String> headers,
      AuthenticationHandler auth) throws IOException {
    HttpUriRequestBase get = new HttpGet(url);
    addHeaders(get, headers);
    return executeRequest(get, auth);
  }

  /**
   * Makes POST request with given url,request, headers and authHandler
   * 
   * @param url request URL
   * @param requestBody body
   * @param headers Request headers
   * @param auth Authentication handler for the request
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException if the request execution fails
   */
  public CloseableHttpResponse post(String url, String requestBody, Map<String, String> headers,
      AuthenticationHandler auth) throws IOException {

    HttpPost post = new HttpPost(url);
    addHeaders(post, headers);
    if (requestBody != null) {
      post.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
    }
    return executeRequest(post, auth);
  }


  /**
   * This method makes PUT request for given URL and JSON body parameter.
   *
   * @param url The url to call for remote request
   * @param jsonBody to be submitted with the request
   * @param auth Authentication handler for the request
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException if the request execution fails
   */
  public CloseableHttpResponse put(String url, String jsonBody, AuthenticationHandler auth)
      throws IOException {
    HttpUriRequestBase put = new HttpPut(url);
    put.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
    return executeRequest(put, auth);

  }

  /**
   * This method makes DELETE request for given identifier URL.
   *
   * @param url The url to call for remote request
   * @param auth Authentication handler for the request
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException if the request execution fails
   */
  public CloseableHttpResponse deleteURL(String url, AuthenticationHandler auth)
      throws IOException {
    HttpUriRequestBase delete = new HttpDelete(url);
    return executeRequest(delete, auth);
  }

  /**
   * This method makes DELETE request for given identifier URL.
   *
   * @param url The url to call for remote request
   * @param jsonBody to be submitted with the request
   * @param auth Authentication handler for the request
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException if the request execution fails
   */
  public CloseableHttpResponse delete(String url, String jsonBody, AuthenticationHandler auth)
      throws IOException {
    HttpUriRequestBase delete = new HttpDelete(url);
    delete.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
    return executeRequest(delete, auth);
  }


  /**
   * execute a request with an authentication handler
   * @param request a post, get, delete, put request
   * @param auth authentication handler
   * @return the response
   * @throws IOException if the request execution fails
   */
  public CloseableHttpResponse executeRequest(HttpUriRequestBase request, AuthenticationHandler auth)
          throws IOException {
    if (auth != null)
      auth.setAuthorization(request);
    return executeHttpClient(request);
  }

  /**
   * Execute the http client for the url passed
   * 
   * @param request url to be execeuted
   * @param <T> class extending HttpUriRequestBase
   * @return response handler of the executed request
   * @throws IOException if the request execution fails
   */
  public <T extends HttpUriRequestBase> CloseableHttpResponse executeHttpClient(T request)
      throws IOException {
    HttpHost host;
    try {
      host = RoutingSupport.determineHost(request);
    } catch (HttpException e) {
      throw new IOException("Cannot determine the targeted host from the provided request", e);
    }
    return (CloseableHttpResponse) httpClient.executeOpen(host, request, null);
  }


  public void close() throws IOException {
    httpClient.close();
  }

}
