package eu.europeana.api.commons_sb3.exception;

public class AuthorizationExtractionException extends Exception {

    public AuthorizationExtractionException(String message) {
	super(message);
    }

    public AuthorizationExtractionException(String message, Throwable th) {
	super(message, th);
    }

}