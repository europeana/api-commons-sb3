package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import org.springframework.http.HttpStatus;

public class InvalidIdException extends EuropeanaI18nApiException {

    /**
     * Three parameters should be sent - EDM class name and the expected format value
     * @param i18nParams
     */
    public InvalidIdException(String[] i18nParams) {
        super(ErrorMessage.ID_INVALID_404, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
