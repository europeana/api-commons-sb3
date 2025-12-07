package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaApiException;
import org.springframework.http.HttpStatus;

/**
 * Invalid Configuration Exception to be used by api
 * @author srishti singh
 * @since 5 december 2025
 */
public class InvalidConfigurationException extends EuropeanaApiException {

    private static final long serialVersionUID = -3607803722931838987L;

    /**
     * Initialise a new exception for which there is no root cause
     *
     * @param message invalid configuration error message
     */
    public InvalidConfigurationException(String message) {
        super("Invalid Configuration : " + message);
    }

    /**
     * We don't want to log the stack trace for this exception
     *
     * @return false
     */
    @Override
    public boolean doLog() {
        return false;
    }

    /**
     * We don't want to log the stack trace for this exception
     *
     * @return false
     */
    @Override
    public boolean doLogStacktrace() {
        return true;
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
