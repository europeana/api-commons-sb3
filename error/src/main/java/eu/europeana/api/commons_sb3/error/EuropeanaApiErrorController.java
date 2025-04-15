package eu.europeana.api.commons_sb3.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Map;

import static eu.europeana.api.commons_sb3.error.EuropeanaErrorConstants.debug;
import static eu.europeana.api.commons_sb3.error.EuropeanaErrorConstants.profile;

/**
 * Add this controller to your application to replace the default Spring-Boot BasicErrorController that generates the
 * 'Whitelabel' error page. This page will return a json error response for all content types.
 */
@RestController
public class EuropeanaApiErrorController extends AbstractErrorController {

    private final EuropeanaApiErrorAttributes errorAttributes;

    @Value("${server.error.include-message:always}")
    private ErrorProperties.IncludeAttribute includeMessage;
    @Value("${server.error.include-stacktrace:on_param}")
    private ErrorProperties.IncludeStacktrace includeStacktrace;
    @Value("${server.error.include-exception:true}")
    private Boolean includeException; // not used but make sure variable is initialized with default value when not configured

    /**
     * Initialize a new controller to handle error output
     * @param errorAttributes auto-wired ApiErrorAttributes (error fields)
     */
    @Autowired
    public EuropeanaApiErrorController(EuropeanaApiErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    /**
     * Override default Spring-Boot error endpoint
     * @param request incoming request
     * @return error object to serialize
     */
    @GetMapping("/error")
    public Map<String, Object> error(HttpServletRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (ErrorProperties.IncludeAttribute.ON_PARAM.equals(includeMessage) && this.getMessageParameter(request)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (ErrorProperties.IncludeStacktrace.ON_PARAM.equals(includeStacktrace) && this.getTraceParameter(request)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, options);
    }

    /**
     * Support returning stacktrace when either 'debug' or 'profile=debug' is added as parameter
     * @param request
     * @return
     */
    @Override
    protected boolean getTraceParameter(HttpServletRequest request) {
        if (this.getBooleanParameter(request, debug)) {
            return true;
        }
        String parameter = request.getParameter(profile);
        if (parameter == null) {
            return false;
        } else {
            return Arrays.stream(parameter.split("[+,]")).anyMatch(debug::equalsIgnoreCase);
        }
    }

}
