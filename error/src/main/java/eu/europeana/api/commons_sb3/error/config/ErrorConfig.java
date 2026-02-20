package eu.europeana.api.commons_sb3.error.config;

// TODO srishti ::  whatever will not be used at the end of implementation remove that
public class ErrorConfig {

    /**
     * A bean with name 'i18nService' and a message source 'COMMON_MESSAGE_SOURCE'
     * should be created in the respective APIs if API want to use the common exceptions.
     *
     * Also, if the respective APIs adds their own messages properties file,
     * that should also be configured in the same bean as another message source.
     *
     *  Example :
     *  1. "classpath:messages" is the API's message source
     *  2. ErrorConfig.COMMON_MESSAGE_SOURCE common message source
     *
     *    @Bean(name = ErrorConfig.BEAN_I18nService)
     *   public I18nService getI18nService() {
     *     ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
     *     messageSource.setBasenames(ErrorConfig.COMMON_MESSAGE_SOURCE, "classpath:messages");
     *     messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
     *     I18nServiceImpl service =  new I18nServiceImpl(messageSource);
     *     return service;
     *   }
     */
    public static final String BEAN_I18nService      = "i18nService";
    public static final String COMMON_MESSAGE_SOURCE = "common_messages";

    // i18nKeys of common_messages property file
    public static final String INVALID_PARAM         = "error.invalid_param";
    public static final String MISSING_PARAM         = "error.missing_param";
    public static final String INVALID_BODY_MISSING  = "error.invalid_body_missing";
    public static final String INVALID_BODY_PATTERN  = "error.invalid_body_pattern";
    public static final String MISSING_APIKEY        = "error.missing_apikey";
    public static final String INVALID_ID            = "error.invalid_id";
    public static final String NOT_FOUND             = "error.not_found";

    public static final String EMPTY_APIKEY          = "error.empty_apikey";
    public static final String KEY_INVALID           = "error.key_invalid";
    public static final String TOKEN_INVALID         = "error.token_invalid";

    public static final String LOCKED_MAINTENANCE      = "error.locked_maintenance";
    public static final String NO_LOCK_IN_EFFECT       = "error.lock_not_in_effect";

    public static final String SERVER_ERROR_UNEXPECTED = "error.server_unexpected_error";


}