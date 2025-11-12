package eu.europeana.api.commons_sb3.exception;

/**
 * This is the base exception class for common services exceptions  
 * @author Sergiu Gordea 
 *
 */
public class CommonServiceException extends Exception{

	/**
	 * 
	 */

	public CommonServiceException(String message){
		super(message);
	}
	
	public CommonServiceException(String message, Throwable th){
		super(message, th);
	}
	
	
}