package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidConfigurationException extends EuropeanaI18nApiException {

    /**
     * @param i18nParams - invalid parameter name
     *                     expected value of the parameter
     *                     invalid value of the parameter from the request (optional)
     */
    public InvalidConfigurationException(List<String> i18nParams) {
        super(ErrorMessage.PARAM_INVALID_400, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
