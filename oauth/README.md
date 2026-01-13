# API Commons for Spring Boot v3 - OAuth module

Steps to add authentication to a Spring-Boot 3 API:

1. The oauth module depends on the error module so make sure you have that setup (see the README.md of that module)

2. Define the following properties in your application.properties
    - authorization api name		(for token validation)
    - token endpoint				(for token validation)
    - auth service public key		(for token validation)
    - grant parameters				(for client + user token validation) 
    - apikey endpoint               (for apikey validation)
   
   and make sure the properties are loaded in your application's configuration (see also next point)

3. Make sure you have a class that `extends BaseAuthorizationService` and add something like the following:
```java
    private static final String BEAN_CLIENT_DETAILS_SERVICE ="commons_oauth2_europeanaClientDetailsService";

    @Value("${auth.api.name:}")
    private String authApiName;
    
    @Value("${auth.token.endpoint:}")
    private String authTokenEndpoint;
    
    @Value("${auth.token.signature:}")
    private String authTokenSignature;
    
    @Value("${auth.token.grant.params:}")
    private String authTokenGrantParams;

    @Value("${auth.apikey.endpoint:}")
    private String authApiKeyEndpoint;
        
    @Override
    protected String getApiName() {
        return this.authApiName;
    }
    
    @Override
    protected String getSignatureKey() {
        return this.authTokenSignature;
    }
    
    @Override
    protected Role getRoleByName(String s) {
        // not sure when this is used
    }

    @Bean(name = BEAN_CLIENT_DETAILS_SERVICE)
    public EuropeanaClientDetailsService getClientDetailsService() {
        EuropeanaClientDetailsService clientDetails = new EuropeanaClientDetailsService();
        if (StringUtils.isNotEmpty(authTokenEndpoint) && StringUtils.isNotEmpty(authTokenGrantParams)) {
            AuthenticationConfig config = new AuthenticationConfig(authTokenEndpoint, authTokenGrantParams);
            clientDetails.setAuthHandler(AuthenticationBuilder.newAuthentication(config));
        } else {
            LOG.warn("Keycloak token endpoint and/or grant parameters are NOT set !! ");
        }
        return clientDetails;
    }
```
**Note1:** If you only need to validate client credentials and not also user credentials then you can leave out the
`auth.token.grant.params` and `auth.token.endpoint` configuration option and leave the `getClientDetailsService()` method empty. In this case you also 
need to override the `isResourceAccessVerificationRequired` method (see code snippet below).
```java
    /**
     * To check only client credentials and not user credentials, the method does not require implementation
     * @return null always
     */
    public EuropeanaClientDetailsService getClientDetailsService() {
        return null;
    }
    
    /**
     * To check only client credentials and not user credentials, this method should return false
     * @param operation 
     * @return false always
     */
    @Override
    protected boolean isResourceAccessVerificationRequired(String operation) {
        return false;
    }
```

**Note 2:** It's recommended to have an option to enable/disable authorization, e.g. for (unit) testing.


4. Add the following annotation to your application `@SpringBootApplication(exclude = { exclude = { ManagementWebSecurityAutoConfiguration.class, SecurityAutoConfiguration.class } })`


