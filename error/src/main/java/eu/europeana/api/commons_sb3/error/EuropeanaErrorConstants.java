package eu.europeana.api.commons_sb3.error;

/**
 * Constants used for generating error resposnes
 */
public class EuropeanaErrorConstants {


    // error fields
    public static final String CONTEXT = "@context";
    public static final  String CONTEXT_VALUE = "http://www.europeana.eu/schemas/context/api.jsonld";
    public static final String TYPE = "type";
    public static final  String TYPE_VALUE = "ErrorResponse";
    public static final String SUCCESS = "success";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String SEE_ALSO = "seeAlso";
    public static final  String SEE_ALSO_VALUE = "https://pro.europeana.eu/page/apis";
    public static final String TIMESTAMP = "timestamp";
    public static final String PATH = "path";
    public static final String TRACE = "trace";
    public static final String CODE = "code";

    // other constants
    public static final String QUERY_PARAM_PROFILE = "profile";
    public static final String QUERY_PARAM_PROFILE_SEPARATOR = ",";
    public static final String PROFILE_DEBUG = "debug";


    private EuropeanaErrorConstants() {
        // empty constructor to prevent initialization
    }
}
