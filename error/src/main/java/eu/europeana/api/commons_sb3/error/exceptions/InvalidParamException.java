package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidParamException extends EuropeanaI18nApiException {

    /**
     * @param i18nParams - invalid parameter name
     *                     expected value of the parameter
     *                     invalid value of the parameter from the request (optional)
     */
    public InvalidParamException(List<String> i18nParams) {
        super(ErrorMessage.PARAM_INVALID_400, i18nParams);
    }

    public InvalidParamException(List<String> i18nParams, Throwable th) {
        super(ErrorMessage.PARAM_INVALID_400, i18nParams, HttpStatus.BAD_REQUEST, th);
    }


    public InvalidParamException(HttpStatus responseStatus, List<String> i18nParams){
        super(ErrorMessage.PARAM_INVALID_400, i18nParams, responseStatus);
    }

    public InvalidParamException(HttpStatus responseStatus, List<String> i18nParams, Throwable th){
        super(ErrorMessage.PARAM_INVALID_400, i18nParams, responseStatus, th);
    }


    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
