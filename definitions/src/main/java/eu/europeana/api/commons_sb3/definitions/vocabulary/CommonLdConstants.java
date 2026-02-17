package eu.europeana.api.commons_sb3.definitions.vocabulary;

/**
 * Common json ld constants used for serialization of json-ld response
 *
 * The naming convention is not followed and rather named how the value exactly is
 * This done to avoid confusion with other class fields that have the
 * same constants but with the different value.
 * @See: org.apache.stanbol.commons.jsonld.JsonLdCommon has the same constants
 *       with "@" appended to it
 * This also helps visualize the JSON response more easily,
 * rather than having to navigate to another class to check the exact value of the constant.
 */
@SuppressWarnings("java:S115")
public interface CommonLdConstants {

	//** common fields **/
	public static final String context = "@context";
	public static final String id      = "id";
	public static final String items   = "items";
	public static final String type    = "type";
	public static final String total   = "total";
	public static final String partOf  = "partOf";
	public static final String prev    = "prev";
	public static final String next    = "next";

	public static final String Collection      = "Collection";
	public static final String CollectionPage = "CollectionPage";
	public static final String ResultPage     = "ResultPage";
	public static final String ResultList     = "ResultList";
	
	// Context values
	public static final String WA_CONTEXT     = "http://www.w3.org/ns/anno.jsonld";
	public static final String EDM_CONTEXT    = "http://www.europeana.eu/schemas/context/edm.jsonld";
	public static final String ENTITY_CONTEXT = "https://api.europeana.eu/schema/context/entity.jsonld";

}