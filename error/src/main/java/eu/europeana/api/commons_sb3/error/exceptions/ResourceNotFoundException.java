package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ResourceNotFoundException extends EuropeanaI18nApiException {

    private Class theClass;

    /**
     * @param theClass -  EDM class name
     * @param i18nParams - identifier requested
     */
    public ResourceNotFoundException(@NotNull Class theClass, List<String> i18nParams) {
        super(theClass, ErrorMessage.NOT_FOUND_404, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.NOT_FOUND;
    }

    public String getTheClassName() {
        return theClass.getSimpleName();
    }
}
