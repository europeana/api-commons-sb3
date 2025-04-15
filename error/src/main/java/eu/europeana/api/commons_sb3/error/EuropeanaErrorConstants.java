package eu.europeana.api.commons_sb3.error;

/**
 * Constants used for generating error resposnes
 */
public class EuropeanaErrorConstants {

    public static final String context             = "@context";
    public static final String type                = "type";
    public static final String success             = "success";
    public static final String status              = "status";
    public static final String error               = "error";
    public static final String message             = "message";
    public static final String seeAlso             = "seeAlso";
    public static final String timestamp           = "timestamp";
    public static final String path                = "path";
    public static final String trace               = "trace";
    public static final String code                = "code";
    public static final  String ErrorResponse      = "ErrorResponse";
    public static final  String ERROR_CONTEXT      = "http://www.europeana.eu/schemas/context/api.jsonld";
    public static final  String SEE_ALSO_VALUE     = "https://pro.europeana.eu/page/apis";
    public static final String profile             = "profile";
    public static final String COMMA               = ",";
    public static final String debug               = "debug";


    private EuropeanaErrorConstants() {
        // empty constructor to prevent initialization
    }
}
