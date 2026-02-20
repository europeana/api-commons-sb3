package eu.europeana.api.commons_sb3.oauth2.service.authorization;

import eu.europeana.api.commons_sb3.definitions.oauth.Role;
import eu.europeana.api.commons_sb3.error.config.ErrorConfig;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import eu.europeana.api.commons_sb3.error.exceptions.ApplicationAuthenticationException;
import eu.europeana.api.commons_sb3.exception.ApiKeyExtractionException;
import eu.europeana.api.commons_sb3.exception.AuthorizationExtractionException;
import eu.europeana.api.commons_sb3.exception.EuropeanaClientRegistrationException;
import eu.europeana.api.commons_sb3.oauth2.utils.OAuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import java.util.*;

/**
 * Base Implementation class of Authorization service
 *
 */
public abstract class BaseAuthorizationService implements AuthorizationService {

    private Logger LOG = LogManager.getLogger(BaseAuthorizationService.class);

    RsaVerifier signatureVerifier;

    protected RsaVerifier getSignatureVerifier() {
        if (signatureVerifier == null) {
            signatureVerifier = new RsaVerifier(getSignatureKey());
        }
        return signatureVerifier;
    }

    /**
     * Authorises read access
     * @param request the full HTTP request
     * @return authentication
     * @throws ApplicationAuthenticationException
     */
    @Override
    public Authentication authorizeReadAccess(HttpServletRequest request)
            throws ApplicationAuthenticationException {
        Authentication authentication = null;
        // check and verify jwt token
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (OAuthUtils.isValidBearerToken(authorization)) {
            // if jwt token submitted
            authentication = authorizeReadByJwtToken(request);
        } else {
            // user id not available, verify apiKey only
            authentication = authorizeReadByApiKey(request);
        }
        return authentication;
    }

    /**
     * Authorize access via apikey
     * @param request http request
     * @return authentication
     * @throws ApplicationAuthenticationException
     */
    private Authentication authorizeReadByApiKey(HttpServletRequest request)
            throws ApplicationAuthenticationException {
        String wsKey;
        // extract api key with other methods
        try {
            wsKey = OAuthUtils.extractApiKey(request);
        } catch (ApiKeyExtractionException | AuthorizationExtractionException e) {
            throw new ApplicationAuthenticationException(ErrorMessage.INVALID_KEY_401, null, null, e);
        }

        // check if empty
        if (StringUtils.isEmpty(wsKey))
            throw new ApplicationAuthenticationException(ErrorMessage.EMPTY_KEY_401);

        // validate api key
        try {
            getClientDetailsService().loadClientByClientId(wsKey);
        } catch (EuropeanaClientRegistrationException e) {
            // invalid api key
            throw new ApplicationAuthenticationException(null, null, null, HttpStatus.valueOf(e.getResult().getHttpStatusCode()) , null, e.getResult());
        } catch (OAuth2Exception e) {
            // validation failed through API Key service issues
            // silently approve request
            LOG.info("Invocation of API Key Service failed. Silently approve apikey: " + wsKey, e);
        }

        // anonymous user, only the client application is verified by API key
        return OAuthUtils.buildReadOnlyAuthenticationToken(getApiName(), wsKey);
    }

    /**
     * Authorize access via jwt token
     * @param request http request
     * @return authentication
     * @throws ApplicationAuthenticationException
     */
    private Authentication authorizeReadByJwtToken(HttpServletRequest request)
            throws ApplicationAuthenticationException {
        Authentication authentication = null;
        try {
            // String jwtToken = OAuthUtils.extractPayloadFromAuthorizationHeader(request, OAuthUtils.);
            Map<String, Object> data =
                    OAuthUtils.extractCustomData(request, getSignatureVerifier(), getApiName());
            String wsKey = OAuthUtils.extractApiKey(data);

            // check if null
            if (wsKey == null)
                throw new ApplicationAuthenticationException(ErrorMessage.MISSING_KEY_401);

            if (data.containsKey(OAuthUtils.USER_ID)) {
                // read access is provided to any authenticated user
                List<Authentication> authList = new ArrayList<Authentication>();
                //for read acccess the resource access is not mandatory
                OAuthUtils.processResourceAccessClaims(getApiName(), data, authList, mustVerifyResourceAccessForRead());
                if (!authList.isEmpty()) {
                    authentication = authList.get(0);
                } else {
                    // for backward compatibility, we allow read access to users that don't have a token
                    // created specifically for current API
                    // TODO: in the future we might still want to verify the scope of the JWT token.
                    authentication = OAuthUtils.buildReadOnlyAuthenticationToken(getApiName(), data,wsKey);
                }
            }
        } catch (ApiKeyExtractionException | AuthorizationExtractionException e) {
            throw new ApplicationAuthenticationException(ErrorMessage.TOKEN_INVALID_401, Arrays.asList(e.getMessage()),
                    HttpStatus.UNAUTHORIZED, e);
        }

        return authentication;
    }

    /*
     * (non-Javadoc)
     *
     * @see eu.europeana.api.commons.service.authorization.AuthorizationService#
     * authorizeWriteAccess(jakarta.servlet.http.HttpServletRequest, java.lang.String)
     */
    public Authentication authorizeWriteAccess(HttpServletRequest request, String operation)
            throws ApplicationAuthenticationException {
        return authorizeOperation(request, operation);
    }

    private Authentication authorizeOperation(HttpServletRequest request, String operation)
            throws ApplicationAuthenticationException {

        //invalid configurations
        if (getSignatureVerifier() == null) {
            throw new ApplicationAuthenticationException(ErrorConfig.OPERATION_NOT_AUTHORIZED,
                    ErrorConfig.OPERATION_NOT_AUTHORIZED,
                    Arrays.asList("No signature key configured for verification of JWT Token"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<? extends Authentication> authenticationList;
        boolean verifyResourceAccess = isResourceAccessVerificationRequired(operation);
        try {
            //parses and validates the jwt token
            authenticationList =
                    OAuthUtils.processJwtToken(request, getSignatureVerifier(), getApiName(), verifyResourceAccess);
        } catch (ApiKeyExtractionException | AuthorizationExtractionException e) {
            throw new ApplicationAuthenticationException(ErrorConfig.OPERATION_NOT_AUTHORIZED,
                    ErrorConfig.OPERATION_NOT_AUTHORIZED, Arrays.asList("Invalid token or ApiKey"),
                    HttpStatus.UNAUTHORIZED, e);
        }

        if (authenticationList == null || authenticationList.isEmpty()) {
            throw new ApplicationAuthenticationException(ErrorConfig.OPERATION_NOT_AUTHORIZED,
                    ErrorConfig.OPERATION_NOT_AUTHORIZED, Arrays.asList("Invalid token or ApiKey, resource access not granted!"),
                    HttpStatus.FORBIDDEN);
        }

        if (verifyResourceAccess) {
            //verify permissions
            return checkPermissions(authenticationList, getApiName(), operation);
        } else {
            //return authenticated user and client
            return authenticationList.get(0);
        }
    }


    /**
     * This method verifies write access rights for particular api and operation
     *
     * @param authenticationList The list of authentications extracted from the JWT token
     * @param api The name of the called api
     * @param operation The name of called api operation
     * @throws ApplicationAuthenticationException if the access to the api operation is not authorized
     */
    @SuppressWarnings("unchecked")
    protected Authentication checkPermissions(List<? extends Authentication> authenticationList,
                                              String api, String operation) throws ApplicationAuthenticationException {

        final boolean isEmptyAuthenticationList = (authenticationList == null || authenticationList.isEmpty());

        if (isEmptyAuthenticationList) {
            if (isResourceAccessVerificationRequired(operation)) {
                //access verification required but
                throw new ApplicationAuthenticationException(ErrorConfig.OPERATION_NOT_AUTHORIZED,
                        ErrorConfig.OPERATION_NOT_AUTHORIZED,
                        Arrays.asList("No or invalid authorization provided"), HttpStatus.FORBIDDEN);
            } else {
                //TODO:
                return null;
            }
        }

        if (authenticationList == null || authenticationList.isEmpty()) {

        }

        List<GrantedAuthority> authorityList;
        for (Authentication authentication : authenticationList) {
            authorityList = (List<GrantedAuthority>) authentication.getAuthorities();
            if (api.equals(authentication.getDetails())
                    && isOperationAuthorized(operation, authorityList)) {
                // access granted
                return authentication;
            }
        }
        // not authorized
        throw new ApplicationAuthenticationException(ErrorConfig.OPERATION_NOT_AUTHORIZED,
                ErrorConfig.OPERATION_NOT_AUTHORIZED,
                Arrays.asList("Operation not permitted or not GrantedAuthority found for operation:" + operation),
                HttpStatus.FORBIDDEN);
    }


    public Authentication checkPermissions(Authentication authentication, String operation)
            throws ApplicationAuthenticationException {
        return checkPermissions(List.of(authentication), getApiName(), operation);
    }


    /**
     * This method performs checking, whether an operation is supported for provided authorities
     *
     * @param operation the called api operation
     * @param authorities the list of granted authorities (as provided through the JWT token)
     * @return true if operation authorized
     */
    private boolean isOperationAuthorized(String operation, List<GrantedAuthority> authorities) {
        if (!isResourceAccessVerificationRequired(operation)) {
            return true;
        }
        Role userRole;
        List<String> allowedOperations;
        for (GrantedAuthority authority : authorities) {
            userRole = getRoleByName(authority.getAuthority());
            if (userRole == null) {
                continue;
            }
            allowedOperations = Arrays.asList(userRole.getPermissions());
            if (allowedOperations.contains(operation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indicate if the resource access needs to be verified for read operations. This indicates if the resourceAccess is available in jwt tokens used for the current API
     * Default is true.
     * @return true if the resourceAceess field needs to be processed for read access
     */
    protected  boolean mustVerifyResourceAccessForRead() {
        return true;
    }

    /**
     * Method to indicate if the resource access (i.e. user has the role which grants permissions for the operation) is required.
     * Client authentication is mandatory, but apis might grant access to all users if the token is valid
     * Api should overwrite this method in order to disable resource access verification
     *
     * @return true if the resource access needs to be verified
     */
    protected boolean isResourceAccessVerificationRequired(String operation) {
        return true;
    }


    /**
     * This method returns the api specific Role for the given role name
     *
     * @param name the name of user role
     * @return the user role
     */
    protected abstract Role getRoleByName(String name);

    /**
     *
     * @return key used to verify the JWT token signature
     */
    protected abstract String getSignatureKey();

    /**
     *
     * @return the service providing the client details
     */
    protected abstract ClientDetailsService getClientDetailsService();

    /**
     *
     * @return the name of the API calling the authorization service
     */
    protected abstract String getApiName();

}