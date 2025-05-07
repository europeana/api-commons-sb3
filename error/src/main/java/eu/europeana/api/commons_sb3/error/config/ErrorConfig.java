package eu.europeana.api.commons_sb3.error.config;

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
    public static final String INVALID_BODY          = "error.invalid_body";
    public static final String INVALID_ID            = "error.invalid_id";
    public static final String NOT_FOUND             = "error.not_found";


}
