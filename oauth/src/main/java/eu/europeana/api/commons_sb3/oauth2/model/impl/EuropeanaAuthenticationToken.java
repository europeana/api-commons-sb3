package eu.europeana.api.commons_sb3.oauth2.model.impl;

import eu.europeana.api.commons_sb3.oauth2.model.ApiCredentials;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * This class maps contents of JwtToken
 * <p>
 * in granted authorities we store roles
 * in details we store the api for which the resource_access is requested (i.e. api_annotations, api_entity)
 *
 * @author GrafR
 */
public class EuropeanaAuthenticationToken extends AbstractAuthenticationToken {

    String principal = null;
    ApiCredentials credentials;

    public static final String DEFAULT_ROLE_USER = "user";

    /**
     * This constructor supports also details
     *
     * @param grantedAuthorities the authorities holding granted access permissions
     * @param api                the API for which access is requested
     * @param principal          the username
     */
    public EuropeanaAuthenticationToken(Collection<? extends GrantedAuthority> grantedAuthorities, String api, String principal, ApiCredentials credentials) {
        super(grantedAuthorities);
        setDetails(api);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}