package eu.europeana.api.commons_sb3.definitions.http;

import org.springframework.http.MediaType;

/**
 * Common headers for all api's
 * @author Srishti Singh
 * @since 20 November 2024
 *
 */
public interface HttpHeaders {

    /**
     * @see <a href="http://www.w3.org/wiki/LinkHeader">W3C Link Header documentation</a>.
     *
     */
    String LINK = "Link";
    String ALLOW = "Allow";

    String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    String PREFER = "Prefer";
    String PREFERENCE_APPLIED = "Preference-Applied";

    String ALLOW_GOH = "GET,OPTIONS,HEAD";
    String ALLOW_POST = "POST";
    String ALLOW_GET = "GET";
    String ALLOW_DELETE = "DELETE";
    String ALLOW_GPuD = "GET,PUT,DELETE";

    String CONTENT_TYPE_JSON_UTF8 = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8";
    String CONTENT_TYPE_JSONLD_UTF8 = "application/ld+json;charset=utf-8";
    String CONTENT_TYPE_JSONLD = "application/ld+json";
    String CONTENT_TYPE_APPLICATION_RDF_XML = "application/rdf+xml";
    String CONTENT_TYPE_RDF_XML = "rdf/xml";

    String VALUE_LDP_RESOURCE = "<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"";
    String VALUE_LDP_CONTAINER = "<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"\n"+
            "<http://www.w3.org/TR/annotation-protocol/constraints>;\n" +
            "rel=\"http://www.w3.org/ns/ldp#constrainedBy\"";


    String RATE_LIMIT_POLICY_HEADER                      = "RateLimit-Policy";
    String RATE_LIMIT_HEADER                             = "RateLimit";

}