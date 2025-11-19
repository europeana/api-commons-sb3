package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.HttpException;
import eu.europeana.api.commons_sb3.error.config.ErrorConfig;
import org.springframework.http.HttpStatus;

public class InternalServerException extends HttpException {

    public static final String MESSAGE_UNEXPECTED_EXCEPTION = "An unexpected server exception occured!";

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InternalServerException(String message, Throwable th){
        super(message, null, null, HttpStatus.INTERNAL_SERVER_ERROR, th);
    }

    public InternalServerException(Throwable th){
        super(th.getMessage(), ErrorConfig.SERVER_ERROR_UNEXPECTED, null, HttpStatus.INTERNAL_SERVER_ERROR, th);
    }

}