package eu.europeana.api.commons_sb3.http;

import eu.europeana.api.commons_sb3.auth.AuthenticationHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class AsyncHttpConnection {

    private final CloseableHttpAsyncClient httpClient;

   public AsyncHttpConnection(boolean withRedirect) {
        if (withRedirect) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setCircularRedirectsAllowed(true)
                    .build();

            this.httpClient = HttpAsyncClients.customHttp2()
                    .setDefaultRequestConfig(requestConfig)
                    .setRedirectStrategy(new DefaultRedirectStrategy()).
                    build();

        } else {
            this.httpClient = HttpAsyncClients.customHttp2().disableRedirectHandling().build();
        }
        httpClient.start();
    }

    /**
     *This method makes GET request for given URL.
     * @param url
     * @param acceptHeaderValue
     * @param auth Authentication handler for the request
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */

    public SimpleHttpResponse get(String url, String acceptHeaderValue
            , AuthenticationHandler auth) throws IOException, ExecutionException, InterruptedException {
        SimpleHttpRequest httpRequest = new SimpleHttpRequest("GET", url);
        System.out.println("version of get  " +httpRequest.getVersion());

        if (StringUtils.isNotBlank(acceptHeaderValue)) {
            httpRequest.setHeader(HttpHeaders.ACCEPT, acceptHeaderValue);
        }
        if (auth!= null)         auth.setAuthorization(httpRequest);

        Future<SimpleHttpResponse> response =  httpClient.execute(httpRequest, null);
        return response.get();
    }

    /**
     * This method makes POST request for given URL and JSON body parameter.
     *
     * @param url
     * @param requestBody
     * @param contentType
     * @param auth Authentication handler for the request
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */
    public HttpResponseHandler post(String url, String requestBody, String contentType
            , AuthenticationHandler auth) throws IOException {
        HttpPost post = new HttpPost(url);
        if(StringUtils.isNotBlank(contentType)) {
            addHeaders(post, HttpHeaders.CONTENT_TYPE, contentType);
        }
        if (auth!= null)         auth.setAuthorization(post);
        if(requestBody != null) {
            post.setEntity(new StringEntity(requestBody));
        }
        return executeHttpClient(post);
    }



    /**
     * This method makes PUT request for given URL and JSON body parameter.
     *
     * @param url
     * @param jsonParamValue
     * @param auth Authentication handler for the request
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */
    public HttpResponseHandler put(String url, String jsonParamValue
            , AuthenticationHandler auth) throws IOException {
        HttpPut put = new HttpPut(url);
        auth.setAuthorization(put);
        put.setEntity(new StringEntity(jsonParamValue));

        return executeHttpClient(put);

    }

    /**
     * This method makes DELETE request for given identifier URL.
     *
     * @param url                       The identifier URL
     * @param auth Authentication handler for the request
     * @return HttpResponseHandler that comprises response body as String and status code.
     * @throws IOException
     */
    public HttpResponseHandler deleteURL(String url, AuthenticationHandler auth) throws IOException {
        HttpDelete delete = new HttpDelete(url);
        auth.setAuthorization(delete);
        return executeHttpClient(delete);
    }


    private <T extends HttpUriRequestBase> HttpResponseHandler executeHttpClient(T url) throws IOException {
        HttpResponseHandler responseHandler = new HttpResponseHandler();
        //httpClient.execute(SimpleHttpRequest.copy(url), null);
        return responseHandler;
    }

    private <T extends HttpUriRequestBase> void addHeaders(T url, String headerName, String headerValue) {
        if (StringUtils.isNotBlank(headerValue)) {
            url.setHeader(headerName, headerValue);
        }
    }

    public void close() throws IOException {
        httpClient.close();
    }
}
