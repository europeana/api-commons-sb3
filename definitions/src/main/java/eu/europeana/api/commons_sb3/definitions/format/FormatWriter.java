package eu.europeana.api.commons_sb3.definitions.format;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public interface FormatWriter<T> {

    /**
     * Method for serialising T value
     * @param value value to be formatted
     * @param out output stream
     * @throws IOException
     */
    void write(T value, OutputStream out) throws IOException;

    /**
     * Method for serialising list of values
     * @param value list of objects to be serialised
     * @param out output stream
     * @throws IOException
     */
    void write(Iterator<T> value, int size, OutputStream out) throws IOException;

}
