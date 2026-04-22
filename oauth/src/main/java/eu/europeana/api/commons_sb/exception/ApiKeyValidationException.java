package eu.europeana.api.commons_sb.exception;

public class ApiKeyValidationException extends Exception {

    public ApiKeyValidationException(String errorMessage) {
        super(errorMessage);
    }

    public ApiKeyValidationException(String errorMessage, Exception cause) {
        super(errorMessage, cause);
    }

}