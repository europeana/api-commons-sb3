package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.ErrorMessage;
import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import org.springframework.http.HttpStatus;

public class InvalidConfigurationException extends EuropeanaI18nApiException {

    /**
     * Three parameters should be sent - param name , expected value and the invalid value from the request ( this is optional)
     * @param i18nParams
     */
    public InvalidConfigurationException(String[] i18nParams) {
        super(ErrorMessage.PARAM_INVALID_400, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }

//    public InvalidConfigurationException(String message, String error, String errorCode, HttpStatus responseStatus, String i18nKey, String[] i18nParams) {
//        super(message, error, errorCode, responseStatus, i18nKey, i18nParams);
//    }
//
//    public InvalidConfigurationException(String message, String error, String errorCode, HttpStatus responseStatus, String i18nKey, String[] i18nParams, Throwable th) {
//        super(message, error, errorCode, responseStatus, i18nKey, i18nParams, th);
//    }
}
