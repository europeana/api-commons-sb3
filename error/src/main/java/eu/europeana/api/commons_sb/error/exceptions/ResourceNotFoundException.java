package eu.europeana.api.commons_sb.error.exceptions;

import eu.europeana.api.commons_sb.error.EuropeanaI18nApiException;
import eu.europeana.api.commons_sb.error.config.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ResourceNotFoundException extends EuropeanaI18nApiException {

    private Class theClass;

    /**
     * Constructs an exception indicating that a duplicate class was found.
     * Error message - "No <Class> was found with the identifier: <identifier>"
     * NOTE: the first param is the <Class> name that is already included via the
     *       class name passed in the constructor. Only pass the second param of the I18key message
     *       which is the <identifier>
     *
     * @param theClass   the EDM class for which the resource was requested
     * @param i18nParams a list of parameters used for internationalized error messages.
     *
     */
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
