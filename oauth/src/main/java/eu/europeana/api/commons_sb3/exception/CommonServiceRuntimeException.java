package eu.europeana.api.commons_sb3.exception;

/**
 * This is the base exception class for (uncaught) common services runtime exceptions
 * @author Sergiu Gordea 
 *
 */
public class CommonServiceRuntimeException extends RuntimeException{

	/**
	 * 
	 */

	public CommonServiceRuntimeException(String message){
		super(message);
	}
	
	public CommonServiceRuntimeException(String message, Throwable th){
		super(message, th);
	}
	
	
}