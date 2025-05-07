package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import org.springframework.http.HttpStatus;

public class MissingParamException extends EuropeanaI18nApiException {

    /**
     * Three parameters should be sent - missing parameter name
     * @param i18nParams
     */
    public MissingParamException(String[] i18nParams) {
        super(ErrorMessage.PARAM_MISSING_400, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
