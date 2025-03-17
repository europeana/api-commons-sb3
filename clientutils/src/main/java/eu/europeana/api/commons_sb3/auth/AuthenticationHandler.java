package eu.europeana.api.commons_sb3.auth;

import org.apache.hc.core5.http.HttpRequest;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
public interface AuthenticationHandler extends AuthenticationConstants {

    void setAuthorization(HttpRequest httpRequest) throws AuthenticationException;
}
