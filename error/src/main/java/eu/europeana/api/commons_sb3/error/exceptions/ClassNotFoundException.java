package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ClassNotFoundException extends EuropeanaI18nApiException {

    /**
     * Three parameters should be sent - EDM class name and the identifier requested
     * @param i18nParams
     */
    public ClassNotFoundException(String[] i18nParams) {
        super(ErrorMessage.NOT_FOUND_404, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
