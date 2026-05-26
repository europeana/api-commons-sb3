package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ApplicationAuthenticationException extends EuropeanaI18nApiException {

    /**
     * By default, the parent class 'EuropeanaApiException' returns the http status 500 if no http status is set.
     * Method considers the status to be 'UNAUTHORIZED'.
     * @param errorMessage
     */
    public ApplicationAuthenticationException(ErrorMessage errorMessage){
        super(errorMessage, null, HttpStatus.UNAUTHORIZED);
    }

    public ApplicationAuthenticationException(ErrorMessage errorMessage, List<String> i18nParams, HttpStatus responseStatus){
        super(errorMessage, i18nParams, responseStatus);
    }

    public ApplicationAuthenticationException(ErrorMessage errorMessage, List<String> i18nParams, HttpStatus responseStatus, Throwable th){
        super(errorMessage, i18nParams, responseStatus, th);
    }


    public ApplicationAuthenticationException(String message, String i18nKey) {
        super(message, null, null, HttpStatus.UNAUTHORIZED, i18nKey, null);
    }

    public ApplicationAuthenticationException(String message, String code, String error, HttpStatus status) {
        super(message, code, error, status, null, null);
    }

    public ApplicationAuthenticationException(String message, String i18nKey, List<String> i18nParams, HttpStatus status, Throwable th) {
        super(message, null, null, status, i18nKey, i18nParams, th);
    }

    public ApplicationAuthenticationException(String message, String i18nKey, List<String> i18nParams) {
        super(message, null, null, HttpStatus.UNAUTHORIZED, i18nKey, i18nParams);
    }

    public ApplicationAuthenticationException(String message, String i18nKey, List<String> i18nParams, HttpStatus status) {
        super(message, null, null, status, i18nKey, i18nParams);
    }

    /**
     * We do not want to log the exception stack trace just the error message
     * @return
     */
    @Override
    public boolean doLogStacktrace() {
        return false;
    }




}