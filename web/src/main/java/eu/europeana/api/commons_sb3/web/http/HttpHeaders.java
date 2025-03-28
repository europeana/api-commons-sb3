package eu.europeana.api.commons_sb3.web.http;

import org.springframework.http.MediaType;

/**
 *
 * @author Srishti Singh
 * @since 20 November 2024
 *
 */
public interface HttpHeaders extends jakarta.ws.rs.core.HttpHeaders {

    /**
     * @see <a href="http://www.w3.org/wiki/LinkHeader">W3C Link Header documentation</a>.
     *
     */
    public static final String LINK = "Link";
    public static final String ALLOW = "Allow";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String PREFER = "Prefer";
    public static final String PREFERENCE_APPLIED = "Preference-Applied";

    public static final String ALLOW_GOH = "GET,OPTIONS,HEAD";
    public static final String ALLOW_POST = "POST";
    public static final String ALLOW_GET = "GET";
    public static final String ALLOW_DELETE = "DELETE";
    public static final String ALLOW_GPuD = "GET,PUT,DELETE";

    public static final String CONTENT_TYPE_JSON_UTF8 = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8";
    public static final String CONTENT_TYPE_JSONLD_UTF8 = "application/ld+json;charset=utf-8";
    public static final String CONTENT_TYPE_JSONLD = "application/ld+json";
    public static final String CONTENT_TYPE_APPLICATION_RDF_XML = "application/rdf+xml";
    public static final String CONTENT_TYPE_RDF_XML = "rdf/xml";

    public static final String VALUE_LDP_RESOURCE = "<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"";
    public static final String VALUE_LDP_CONTAINER = "<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"\n"+
            "<http://www.w3.org/TR/annotation-protocol/constraints>;\n" +
            "rel=\"http://www.w3.org/ns/ldp#constrainedBy\"";

}