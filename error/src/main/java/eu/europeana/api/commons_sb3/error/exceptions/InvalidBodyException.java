package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidBodyException extends EuropeanaI18nApiException {

    /**
     * Three parameters should be sent - all the invalid parameter names
     * @param i18nParams
     */
    public InvalidBodyException(List<String> i18nParams) {
        super(ErrorMessage.BODY_INVALID_400, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
