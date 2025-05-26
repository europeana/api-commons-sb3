package eu.europeana.api.commons_sb3.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static eu.europeana.api.commons_sb3.error.EuropeanaErrorConstants.*;

/**
 * The default Spring-Boot error response where the EuropeanaGlobalExceptionHandler doesn't handle it (e.g. 404s).
 * Make sure this class is loaded by Spring.
 */
@Component("europeanaApiErrorAttributes")
public class EuropeanaApiErrorAttributes extends DefaultErrorAttributes {

    @Value("${server.error.see-also:#{null}}")
    private String seeAlsoValue;

    @Value("${server.error.context:#{null}}")
    private String errorContext;

    /**
     * Used by Spring to display errors with no custom handler.
     * Since we explicitly return {@link EuropeanaApiErrorResponse} on errors within controllers, this method is only invoked when
     * a request isn't handled by any controller.
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions sbOptions) {
        // get default error attributes from Spring Boot
        final Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, sbOptions);

        // use LinkedHashMap to guarantee display order
        LinkedHashMap<String, Object> europeanaErrorAttributes = new LinkedHashMap<>();

        if (StringUtils.hasLength(errorContext)) {
            europeanaErrorAttributes.put(context, errorContext);
        }
        europeanaErrorAttributes.put(type, ErrorResponse);
        europeanaErrorAttributes.put(success, false);
        europeanaErrorAttributes.put(status, defaultErrorAttributes.get(status)); // http response code
        europeanaErrorAttributes.put(error, defaultErrorAttributes.get(error)); // short error message
        addCodeFieldIfAvailable(europeanaErrorAttributes, webRequest);
        if (defaultErrorAttributes.get(message) != null) {
            europeanaErrorAttributes.put(message, defaultErrorAttributes.get(message)); // human readable description
        }
        //to be enabled when the URL is available, eventually through application configuration
        if (StringUtils.hasLength(seeAlsoValue)) {
            europeanaErrorAttributes.put(seeAlso, seeAlsoValue);
        }
        europeanaErrorAttributes.put(timestamp, OffsetDateTime.now());
        addPathRequestParameters(europeanaErrorAttributes, webRequest);
        if (defaultErrorAttributes.get(trace) != null) {
            europeanaErrorAttributes.put(trace, defaultErrorAttributes.get(trace)); // stacktrace
        }
        return europeanaErrorAttributes;
    }

    /**
     * If the error is an EuropeanaApiException and contains an error code, we add that to the error
     */
    private void addCodeFieldIfAvailable(Map<String, Object> errorAttributes, WebRequest webRequest) {
        final Throwable throwable = super.getError(webRequest);
        if (throwable instanceof EuropeanaApiException apiException &&
                StringUtils.hasLength(apiException.getErrorCode())) {
            errorAttributes.put(code, apiException.getErrorCode());
        }
    }

    /**
     * Spring errors only return the error path and not the parameters, so we add those ourselves.
     * The original parameter string is not available in WebRequest so we rebuild it.
     */
    // TODOO it seems that with the Switch to Spring Boot 3 the path is no longer available!?
    private void addPathRequestParameters(Map<String, Object> errorAttributes, WebRequest webRequest) {
        Iterator<String> it = webRequest.getParameterNames();
        StringBuilder params = new StringBuilder();
        while (it.hasNext()) {
            if (params.length() == 0) {
                params.append('?');
            } else {
                params.append("&");
            }
            String paramName = it.next();
            params.append(paramName);
            String paramValue = webRequest.getParameter(paramName);
            if (StringUtils.hasText(paramValue)) {
                params.append("=").append(paramValue);
            }
        }
        if (params.length() > 0) {
            errorAttributes.put(path, errorAttributes.get(path) + params.toString());
        }
    }


}
