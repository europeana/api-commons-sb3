package eu.europeana.api.commons_sb3.auth.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.TimeUnit;

import static eu.europeana.api.commons_sb3.auth.AuthenticationConstants.*;
import static eu.europeana.api.commons_sb3.auth.AuthenticationConstants.BUFFER_TIME_IN_SECONDS;
import static eu.europeana.api.commons_sb3.auth.service.GrantConstants.*;
import static eu.europeana.api.commons_sb3.auth.service.GrantConstants.access_token;
import static eu.europeana.api.commons_sb3.auth.service.GrantConstants.expires_in;
import static eu.europeana.api.commons_sb3.auth.service.GrantConstants.refresh_expires_in;
import static eu.europeana.api.commons_sb3.auth.service.GrantConstants.refresh_token;
import static eu.europeana.api.commons_sb3.auth.service.GrantConstants.scope;
import static eu.europeana.api.commons_sb3.auth.service.GrantConstants.session_state;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public record TokenResponse(
        @JsonProperty(access_token) String accessToken
        , @JsonProperty(expires_in) Long expirationTime
        , @JsonProperty(refresh_token) String refreshToken
        , @JsonProperty(refresh_expires_in) Long refreshExpirationTime
        , @JsonProperty(scope) String tokenScope
        , @JsonProperty(session_state) String sessionState) {

    @JsonCreator
    public TokenResponse {
        long now = now();

         expirationTime        = getExpirationTime(now, expirationTime);
         refreshExpirationTime = getExpirationTime(now, refreshExpirationTime);

    }

    /**
     * expires = current time + expires_in (from jwt token request) - adjustment
     * @param now
     * @param expiresIn
     * @return
     */
    private static long getExpirationTime(long now, long expiresIn) {
        return (now + expiresIn - BUFFER_TIME_IN_SECONDS);
    }

    // present time in seconds
    private static long now() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    /**
     * Checks if the token has expired or not
     * @return
     */
    public boolean hasTokenExpired() {
        return (expirationTime() < now());
    }

    public boolean hasRefreshTokenExpired() {
        return (refreshExpirationTime() < now());
    }

    public boolean canRefresh() {
        return ( refreshToken() != null && !hasRefreshTokenExpired() );
    }
}