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
     * For reference : IIIF presentation API
     */
    public static final String BEAN_I18nService      = "i18nService";
    public static final String COMMON_MESSAGE_SOURCE = "common_messages";

    // i18nKeys of common_messages property file
    public static final String INVALID_PARAM         = "error.invalid_param";
    public static final String MISSING_PARAM         = "error.missing_param";
    public static final String INVALID_BODY_MISSING  = "error.invalid_body_missing";
    public static final String INVALID_BODY_PATTERN  = "error.invalid_body_pattern";
    public static final String EMPTY_APIKEY = "error.empty_apikey";
    public static final String MISSING_APIKEY = "error.missing_apikey";
    public static final String INVALID_JWTTOKEN = "error.invalid_jwttoken";
    public static final String OPERATION_NOT_AUTHORIZED = "error.operation_not_authorized";
    public static final String INVALID_APIKEY = "error.invalid_apikey";
    public static final String INVALID_ID            = "error.invalid_id";
    public static final String NOT_FOUND             = "error.not_found";
    public static final String LIMIT_PERSONAL        = "error.limit_personal";
    public static final String LIMIT_PROJECT         = "error.limit_project";

    public static final String KEY_INVALID           = "error.key_invalid";
    public static final String KEY_DISABLED          = "error.key_disabled";
    public static final String TOKEN_MISSING         = "error.token_missing";
    public static final String TOKEN_EXPIRED         = "error.token_expired";
    public static final String TOKEN_INVALID         = "error.token_invalid";
    public static final String SCOPE_MISSING         = "error.scope_missing";
    public static final String CLIENT_NOT_PUBLIC     = "error.client_not_public";
    public static final String USER_MISSING          = "error.user_missing";
    public static final String USER_NOT_AUTHORISED   = "error.user_not_authorised";


    public static final String LOCKED_MAINTENANCE = "error.userset_lock_maintenance";

    public static final String SERVER_ERROR_UNEXPECTED = "error.server_unexpected_error";


}