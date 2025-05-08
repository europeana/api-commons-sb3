package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import org.springframework.http.HttpStatus;

import java.util.List;

public class MissingParamException extends EuropeanaI18nApiException {

    /**
     * @param i18nParams - the mandatory parameter missing
     */
    public MissingParamException(List<String> i18nParams) {
        super(ErrorMessage.PARAM_MISSING_400, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
