package eu.europeana.api.commons_sb3.exception;

public class ApiKeyExtractionException extends Exception {

	public ApiKeyExtractionException(String message){
		super(message);
	}

	public ApiKeyExtractionException(String message, Throwable th){
		super(message, th);
	}

}