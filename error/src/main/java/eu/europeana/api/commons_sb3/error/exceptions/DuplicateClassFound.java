package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

import java.util.List;

public class DuplicateClassFound extends EuropeanaI18nApiException {

    private Class theClass;

    /**
     * Constructs an exception indicating that a duplicate class was found.
     * Error message - "Another <Class> already exists with the same <Matching Criteria>"
     * NOTE: the first param is the <Class> name that is already included via the
     *       class name passed in the constructor. Only pass the second param of the I18key message
     *       which is the <Matching Criteria>
     *
     * @param theClass   the class that was found to have duplicates
     * @param i18nParams a list of parameters used for internationalized error messages.
     *
     */
    public DuplicateClassFound(@NotNull Class theClass, List<String> i18nParams) {
        super(theClass, ErrorMessage.DUPLICATE_CLASS_400, i18nParams);
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    public String getTheClassName() {
        return theClass.getSimpleName();
    }
}

