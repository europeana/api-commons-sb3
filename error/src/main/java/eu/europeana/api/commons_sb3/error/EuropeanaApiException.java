package eu.europeana.api.commons_sb3.error;


import org.springframework.http.HttpStatus;

/**
 * Base error class for Europeana APIs. All other application errors should extend this class
 */
public class EuropeanaApiException extends Exception {

    private static final long serialVersionUID = -1354471712894853562L;

    private final String errorCode;
    private HttpStatus responseStatus;

    /**
     * Initialise a new exception with error code
     * @param msg error message
     * @param errorCode error code (optional)
     * @param t root cause exception
     */
    public EuropeanaApiException(String msg, String errorCode, Throwable t) {
        super(msg, t);
        this.errorCode = errorCode;
    }

    /**
     * Initialise a new exception
     * @param msg error message
     * @param t root cause exception
     */
    public EuropeanaApiException(String msg, Throwable t) {
        this(msg, null, t);
    }

    /**
     * Initialise a new exception with error code for which there is no root cause
     * @param msg error message
     * @param errorCode error code (optional)
     */
    public EuropeanaApiException(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    /**
     * Initialise a new exception for which there is no root cause
     * @param msg error message
     */
    public EuropeanaApiException(String msg) {
        super(msg);
        this.errorCode = null;
    }

    /**
     * By default we log all exceptions, but you can override this method and return false if you do not want an error
     * subclass to log the error
     * @return boolean indicating whether this type of exception should be logged or not.
     */
    public boolean doLog() {
        return true;
    }

    /**
     * By default we log error stacktraces, but you can override this method and return false if you do not want an error
     * subclass to log the error (e.g. in case of common user errors). Note that this only works if doLog is enabled
     * as well.
     * @return boolean indicating whether the stacktrace of the exception should be logged or not.
     */
    public boolean doLogStacktrace() {
        return true;
    }

    /**
     * Indicates whether the error message should be included in responses. This is set to true by default.
     * @return boolean indicating whether the exception message should be exposed to end users
     */
    public boolean doExposeMessage() {
        return true;
    }

    /**
     * @return the error code that was optionally provided when creating this exception
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * Gets the HTTP status for this exception.
     * By default HttpStatus.INTERNAL_SERVER_ERROR is returned (if no different status code was set)
     *
     * @return HTTP status for exception
     */
    public HttpStatus getResponseStatus() {
        return responseStatus!=null ? responseStatus : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * Sets the status to be used in the API response
     * @param statusCode the http status code to set
     */
    public void setResponseStatus(HttpStatus statusCode) {
      this.responseStatus = statusCode;
    }
}
