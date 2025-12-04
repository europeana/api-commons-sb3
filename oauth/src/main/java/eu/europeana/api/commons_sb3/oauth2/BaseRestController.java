package eu.europeana.api.commons_sb3.oauth2;

import eu.europeana.api.commons_sb3.definitions.caching.CachingHeaders;
import eu.europeana.api.commons_sb3.error.AbstractRequestPathMethodService;
import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.exceptions.ApplicationAuthenticationException;
import eu.europeana.api.commons_sb3.error.exceptions.InvalidParamException;
import eu.europeana.api.commons_sb3.oauth2.service.authorization.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public abstract class BaseRestController {

    protected abstract AuthorizationService getAuthorizationService();

    /**
     * If apikey service url is empty, disable the authentication
     * @return
     */
    public boolean isAuthEnabled(String apikyServiceUrl) {
        return StringUtils.isNotEmpty(apikyServiceUrl);
    }

    /**
     * This method adopts KeyCloack token from HTTP request and verifies write
     * access rights for particular api and operation
     *
     * @param request   The HTTP request
     * @param operation The name of current operation
     * @return authentication object containing user token
     * @throws ApplicationAuthenticationException
     */
    public Authentication verifyWriteAccess(String operation, HttpServletRequest request)
            throws ApplicationAuthenticationException {
        return getAuthorizationService().authorizeWriteAccess(request, operation);
    }

    /**
     * Processes the HTTP request and validates the provided APIKey (see also
     * Europeana APIKEY service)
     *
     * @param request the full HTTP request
     * @throws ApplicationAuthenticationException if the APIKey was not submitted
     *                                            with the request or the APIKey
     *                                            could not be validated
     */
    public Authentication verifyReadAccess(HttpServletRequest request) throws ApplicationAuthenticationException {
        return getAuthorizationService().authorizeReadAccess(request);
    }

    /**
     * This method compares If-Match header with the current etag value.
     *
     * @param etag    The current etag value
     * @param request The request containing If-Match header
     * @throws EuropeanaI18nApiException
     */
    public void checkIfMatchHeader(String etag, HttpServletRequest request) throws EuropeanaI18nApiException {
        String ifMatchHeader = request.getHeader(CachingHeaders.IF_MATCH);
        if (ifMatchHeader != null && !ifMatchHeader.equals(etag)) {
            //if the tags doesn't match throw exception
            throw new InvalidParamException(HttpStatus.PRECONDITION_FAILED, Arrays.asList(CachingHeaders.IF_MATCH, ifMatchHeader, etag));
        }
    }

    /**
     * This method generates etag for response header.
     *
     * @param timestamp The date of the last modification
     * @param format    The MIME format
     * @param version   The API version
     * @return etag value
     */
    public String generateETag(Date timestamp, String format, String version) {
        // add timestamp, format and version to an etag
        long serialCode = timestamp.getTime() + format.hashCode();
        if(version != null) {
            serialCode += version.hashCode();
        } else {
            serialCode += "0.0.1-SNAPSHOT".hashCode();
        }

        return "\"" + Long.toHexString(serialCode)  + "\"";
    }

    /**
     * Gets all HTTP methods implemented across the application, that match the url pattern for the
     * current request. This is useful in populating the HTTP Allow header when generating API
     * responses
     */
    protected String getMethodsForRequestPattern(HttpServletRequest request,
                                                 AbstractRequestPathMethodService requestMethodService) {
        if(requestMethodService != null) {
            Optional<String> methodsForRequestPattern =
                    requestMethodService.getMethodsForRequestPattern(request);
            return methodsForRequestPattern.orElse(request.getMethod());
        }else {
            return request.getMethod();
        }
    }

}