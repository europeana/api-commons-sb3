package eu.europeana.api.commons_sb3.definitions.utils;

import java.lang.reflect.Field;
import org.apache.jena.riot.lang.rdfxml.ReaderRDFXML_ARP1;

/**
 * Utility class for RDF handling using Apache Jena's ARP (ARP1) implementation.
 *
 * The class provides a method to disable the validation of spaces in URIs
 * by manipulating the internal configuration of the Jena ARP parser through reflection.
 */
public class RiotRdfUtils {


    /**
     * Disables the error-handling mechanism in Apache Jena's ARP parser that raises errors for URIs containing spaces.
     *
     * This method uses reflection to modify the internal configuration of the
     * {@code ReaderRDFXML_ARP1} class and sets the {@code errorForSpaceInURI} field to {@code false}.
     * It allows the ARP parser to process URIs with spaces without throwing errors.
     *
     * @throws NoSuchFieldException if the {@code errorForSpaceInURI} field cannot be found in the
     *         {@code ReaderRDFXML_ARP1} class.
     * @throws IllegalAccessException if access to the {@code errorForSpaceInURI} field is denied.
     */
    public static void disableErrorForSpaceURI() throws NoSuchFieldException, IllegalAccessException {
        Field f = ReaderRDFXML_ARP1.class.getDeclaredField("errorForSpaceInURI");
        f.setAccessible(true);
        f.set(null, false);
    }
}

