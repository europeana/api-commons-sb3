package eu.europeana.api.commons_sb3.auth;

import eu.europeana.api.commons_sb3.auth.apikey.ApikeyBasedAuthentication;
import eu.europeana.api.commons_sb3.auth.service.AuthenticationService;
import eu.europeana.api.commons_sb3.auth.token.LongLastingTokenAuthentication;
import eu.europeana.api.commons_sb3.auth.token.StaticTokenAuthentication;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Hugo
 * @since 13 Mar 2025
 */
public class AuthenticationBuilder {

    public static AuthenticationHandler newAuthentication(AuthenticationConfig config) {
        String accessToken = config.getAccessToken();
        if ( StringUtils.isNotBlank(accessToken) ) {
            return new StaticTokenAuthentication(accessToken);
        }

        String endpointUri = config.getAuthTokenEndpoitUri();
        if ( StringUtils.isNotBlank(endpointUri) ) {
            AuthenticationService service = new AuthenticationService(endpointUri);
            return new LongLastingTokenAuthentication(service, config.getAuthGrant());
        }

        String apikey = config.getApiKey();
        if ( StringUtils.isNotBlank(apikey) ) {
            return new ApikeyBasedAuthentication(apikey);
        }

        throw new AuthenticationException(AuthenticationException.CONFIG_MISSING);
    }

}
