package eu.europeana.api.commons_sb3.http;

import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.reactor.IOReactorConfig;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * class that creates basic CloseableHttpAsyncClient.
 * 
 * @author srishti singh
 * @since 25 May 2025
 */
public class AsyncHttpConnection extends AbstractHttpConnection {

  private final CloseableHttpAsyncClient httpClient;

  /**
   * Creates an AsyncHttpConnection without redirect strategy
   */
  public AsyncHttpConnection() {
    this(false);
  }


  /**
   * By default - DefaultRedirectStrategy is initialised if nothing is set. To create a connection
   * without following redirects, set 'redirectHandlingDisabled' to false
   * 
   * @param withRedirect whether the redirects should be followed or not
   */
  public AsyncHttpConnection(boolean withRedirect) {
    // creates a default pool of DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5 and
    // DEFAULT_MAX_TOTAL_CONNECTIONS = 25
    PoolingAsyncClientConnectionManager connPool = new PoolingAsyncClientConnectionManager();
    if (withRedirect) {
      RequestConfig requestConfig = RequestConfig.custom().build();

      this.httpClient = HttpAsyncClients.custom().setConnectionManager(connPool)
          .setDefaultRequestConfig(requestConfig).setRedirectStrategy(new DefaultRedirectStrategy())
          .build();

    } else {
      this.httpClient = HttpAsyncClients.custom().setConnectionManager(connPool)
          .disableRedirectHandling().build();
    }
  }

  /**
   * Creates the async client with the desired connection pool settings and request configs
   * 
   * @param connectionPool connection manager for the client
   * @param requestConfig custom request config for the client
   * @param reactorConfig Custom IO Reactor config for the client
   * @param withRedirect true, client will follow redirection
   */
  public AsyncHttpConnection(PoolingAsyncClientConnectionManager connectionPool,
      RequestConfig requestConfig, IOReactorConfig reactorConfig, boolean withRedirect) {
    HttpAsyncClientBuilder builder = HttpAsyncClients.custom().setConnectionManager(connectionPool)
        .setIOReactorConfig(reactorConfig).setDefaultRequestConfig(requestConfig);
    if (withRedirect) {
      this.httpClient = builder.setRedirectStrategy(new DefaultRedirectStrategy()).build();
    } else {
      this.httpClient = builder.disableRedirectHandling().build();
    }
  }

  /**
   * we need to start the async client before using it; without that, we would get the following
   * exception: java.lang.IllegalStateException: Request cannot be executed; I/O reactor status:
   * INACTIVE
   */
  public void start() {
    this.httpClient.start();
  }

  /**
   * This method makes GET request for given URL.
   * 
   * @param url
   * @param acceptHeaderValue
   * @param auth Authentication handler for the request
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException
   */

  public SimpleHttpResponse get(String url, String acceptHeaderValue, AuthenticationHandler auth)
      throws ExecutionException, InterruptedException {
    SimpleHttpRequest httpRequest = new SimpleHttpRequest("GET", url);
    if (StringUtils.isNotBlank(acceptHeaderValue)) {
      httpRequest.setHeader(HttpHeaders.ACCEPT, acceptHeaderValue);
    }
    return executeRequest(httpRequest, auth);
  }

  /**
   * This method makes a POST request for given URL and JSON body parameter.
   *
   * @param url request url
   * @param requestBody body
   * @param contentType cotent type of the body
   * @param auth Authentication handler for the request
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException if the request execution fails
   */
  public SimpleHttpResponse post(String url, String requestBody, String contentType,
      AuthenticationHandler auth) throws ExecutionException, InterruptedException {
    SimpleHttpRequest httpRequest = new SimpleHttpRequest("POST", url);
    if (StringUtils.isNotBlank(contentType)) {
      httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
    }
    if (requestBody != null) {
      httpRequest.setBody(requestBody, ContentType.create(contentType));
    }
    
    return executeRequest(httpRequest, auth);
  }

  /**
   * This method makes a PUT request for given URL and JSON body parameter.
   *
   * @param url the request URL
   * @param jsonBody the body
   * @param auth Authentication handler for the request
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException if the request execution fails
   */
  public SimpleHttpResponse put(String url, String jsonBody, AuthenticationHandler auth)
      throws ExecutionException, InterruptedException {
    SimpleHttpRequest httpRequest = new SimpleHttpRequest("PUT", url);
    if (jsonBody != null) {
      httpRequest.setBody(jsonBody, null);
    }
    return executeRequest(httpRequest, auth);
  }

  /**
   * This method makes a DELETE request for a given identifier URL.
   *
   * @param url The identifier URL
   * @param auth Authentication handler for the request (should not be null for deletion)
   * @return HttpResponseHandler that comprises response body as String and status code.
   * @throws IOException if the request execution fails
   */
  public SimpleHttpResponse deleteURL(String url, AuthenticationHandler auth)
      throws ExecutionException, InterruptedException {
    SimpleHttpRequest httpRequest = new SimpleHttpRequest("DELETE", url);
    return executeRequest(httpRequest, auth);
  }

  /**
   * Executes the provided HTTP request using the configured HTTP client. If an {@code AuthenticationHandler}
   * is provided, it will set the necessary authorization headers for the request before execution.
   *
   * @param httpRequest the HTTP request to be executed
   * @param auth the authentication handler used to set authorization for the request; can be null
   * @return the response of the HTTP request execution
   * @throws InterruptedException if the execution is interrupted
   * @throws ExecutionException if an execution error occurs during the request
   */
  SimpleHttpResponse executeRequest(SimpleHttpRequest httpRequest, AuthenticationHandler auth)
          throws InterruptedException, ExecutionException {
    if (auth != null) {
      auth.setAuthorization(httpRequest);
    }
    return httpClient.execute(httpRequest, null).get();
  }

  /**
   * Close the httpclient
   * 
   * @throws IOException if the closing of client connection fails
   */
  public void close() throws IOException {
    httpClient.close();
  }
}
