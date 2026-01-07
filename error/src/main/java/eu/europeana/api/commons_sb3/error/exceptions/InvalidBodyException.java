package eu.europeana.api.commons_sb3.error.exceptions;

import eu.europeana.api.commons_sb3.error.EuropeanaApiException;
import eu.europeana.api.commons_sb3.error.config.ErrorConfig;
import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvalidBodyException extends EuropeanaApiException {

    Map<String, List<String>> i18KeysAndParams = new HashMap<>();

    /**
     * Can contain multiple messages for the invalid body exceptions
     * @param i18KeysAndParams map of multiple i18Keys and corresponding params for the key message
     */
    public InvalidBodyException(Map<String, List<String>> i18KeysAndParams) {
        super(null, ErrorMessage.BODY_INVALID_400.getError(), ErrorMessage.BODY_INVALID_400.getCode());
        this.i18KeysAndParams = i18KeysAndParams;
    }

    /**
     * Can contain multiple messages for the invalid body exceptions
     * @param i18KeysAndParams map of multiple i18Keys and corresponding params for the key message
     */
    public InvalidBodyException(Map<String, List<String>> i18KeysAndParams, Throwable th) {
        super(null, ErrorMessage.BODY_INVALID_400.getError(), ErrorMessage.BODY_INVALID_400.getCode(), th);
        this.i18KeysAndParams = i18KeysAndParams;
    }

    /**
     * @param i18nParams - missing body parameter
     */
    public InvalidBodyException(String i18nParams) {
        super(null, ErrorMessage.BODY_INVALID_400.getError(), ErrorMessage.BODY_INVALID_400.getCode());
        this.i18KeysAndParams.put(ErrorConfig.INVALID_BODY_MISSING, Arrays.asList(i18nParams));
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    public Map<String, List<String>> getI18KeysAndParams() {
        return i18KeysAndParams;
    }
}
