package eu.europeana.api.commons_sb3.exception;

public class ApiKeyValidationException extends Exception {

    public ApiKeyValidationException(String errorMessage) {
        super(errorMessage);
    }

    public ApiKeyValidationException(String errorMessage, Exception cause) {
        super(errorMessage, cause);
    }

}