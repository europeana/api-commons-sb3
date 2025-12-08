package eu.europeana.api.commons_sb3.error;

import eu.europeana.api.commons_sb3.definitions.oauth.KeyValidationResult;
import eu.europeana.api.commons_sb3.definitions.oauth.exception.ApiWriteLockException;
import eu.europeana.api.commons_sb3.error.config.ErrorConfig;
import eu.europeana.api.commons_sb3.error.exceptions.ApplicationAuthenticationException;
import eu.europeana.api.commons_sb3.error.exceptions.InvalidBodyException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import eu.europeana.api.commons_sb3.error.i18n.I18nService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static eu.europeana.api.commons_sb3.error.config.ErrorConfig.BEAN_I18nService;

/**
 * Global exception handler that catches all errors and logs the interesting ones
 * To use this, create a new class in your application that extends this class and add the @ControllerAdvice annotation
 * to it.
 */
@Component
public class EuropeanaGlobalExceptionHandler {

    @Value("${server.error.include-stacktrace:ON_PARAM}")
    private ErrorProperties.IncludeStacktrace includeStacktraceConfig;

    private static final Logger LOG = LogManager.getLogger(EuropeanaGlobalExceptionHandler.class);

    protected AbstractRequestPathMethodService requestPathMethodService;

    @Resource(name = BEAN_I18nService)
    protected I18nService i18nService;

    /**
     * Checks if {@link EuropeanaApiException} instances should be logged or not
     *
     * @param e exception
     */
    protected void logException(EuropeanaApiException e) {
        if (e.doLog()) {
            if (e.doLogStacktrace()) {
                LOG.error("Caught exception", e);
            } else {
                LOG.error("Caught exception: {}", e.getMessage());
            }
        }
    }

    /**
     * Checks whether stacktrace for exceptions should be included in responses
     * @return true if IncludeStacktrace config is not disabled on server
     */
    protected boolean stackTraceEnabled(){
        return includeStacktraceConfig != ErrorProperties.IncludeStacktrace.NEVER;
    }

    /**
     * Default handler for EuropeanaApiException types
     *
     * @param e caught exception
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleEuropeanaBaseException(EuropeanaApiException e, HttpServletRequest httpRequest) {
        logException(e);
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(e.getResponseStatus().value())
                .setError(e.getResponseStatus().getReasonPhrase())
                .setMessage(e.doExposeMessage() ? e.getMessage() : null)
                .setCode(e.getErrorCode())
                .build();

        return ResponseEntity
                .status(e.getResponseStatus())
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * Handler for InvalidBodyException types
     *
     * @param e caught exception
     */
    @ExceptionHandler(InvalidBodyException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleEuropeanaApiException(InvalidBodyException e, HttpServletRequest httpRequest) {
        EuropeanaApiErrorResponse response =
                new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                        .setStatus(e.getResponseStatus().value())
                        .setError(e.getError())
                        .setMessage(buildResponseMessage(e, e.getI18KeysAndParams()))
                        .setCode(e.getErrorCode())
                        .build();
        LOG.error("Caught exception: {}", response.getMessage());
        return ResponseEntity.status(e.getResponseStatus()).headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * Handler for ApiWriteLockException types
     *
     * @param e caught exception
     */
    @ExceptionHandler(ApiWriteLockException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleEuropeanaApiException(ApiWriteLockException e, HttpServletRequest httpRequest) {
        EuropeanaApiErrorResponse response =
                new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                        .setStatus(HttpStatus.LOCKED.value())
                        .setError("Locked for maintenance")
                        .setMessage(buildResponseMessage(e, ErrorConfig.LOCKED_MAINTENANCE, Arrays.asList(e.getMessage())))
                        .setCode("423_locked_maintenance")
                        .build();
        LOG.error("Caught exception: {}", response.getMessage());
        return ResponseEntity.status(HttpStatus.LOCKED).headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    private String buildResponseMessage(Exception e, Map<String, List<String>> i18KeysAndParams) {
        if (i18nService != null && !i18KeysAndParams.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (Map.Entry<String,List<String>> entry : i18KeysAndParams.entrySet()) {
                message.append(buildResponseMessage(e, entry.getKey(), entry.getValue()));
                message.append(" \n ");
            }
            return message.toString();
        } else {
            return e.getMessage();
        }
    }


    /**
     * Handler for EuropeanaI18nApiException types
     *
     * @param e caught exception
     */
    @ExceptionHandler(EuropeanaI18nApiException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleEuropeanaApiException(EuropeanaI18nApiException e, HttpServletRequest httpRequest) {
        EuropeanaApiErrorResponse response =
                new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                        .setStatus(e.getResponseStatus().value())
                        .setError(e.getError())
                        .setMessage(buildResponseMessage(e, e.getI18nKey(), e.getI18nParams()))
                        .setCode(e.getErrorCode())
                        .build();
        LOG.error("Caught exception: {}", response.getMessage());
        return ResponseEntity.status(e.getResponseStatus()).headers(createHttpHeaders(httpRequest))
                .body(response);
    }


    protected String buildResponseMessage(Exception e, String i18nKey, List<String> i18nParams) {
        if (i18nService != null && StringUtils.isNotBlank(i18nKey)) {
            return i18nService.getMessage(i18nKey, i18nParams.toArray(new String[0]));
        } else {
            return e.getMessage();
        }
    }

    /**
     * Handler for HttpRequestMethodNotSupportedException errors
     * Make sure we return 405 instead of 500 response when http method is not supported; also include error message
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleHttpMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest httpRequest) {
        HttpStatus responseStatus = HttpStatus.METHOD_NOT_ALLOWED;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setMessage(e.getMessage())
                .build();

        Set<HttpMethod> supportedMethods = e.getSupportedHttpMethods();

        // set Allow header in error response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (supportedMethods != null) {
            headers.setAllow(supportedMethods);
        }
        return new ResponseEntity<>(response, headers, responseStatus);
    }


    /**
     * Handler for ConstraintValidation errors
     * Make sure we return 400 instead of 500 response when input validation fails; also include error message
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleInputValidationError(ConstraintViolationException e, HttpServletRequest httpRequest) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(responseStatus)
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * MissingServletRequestParameterException thrown when a required parameter is not included in a request.
     */
    @ExceptionHandler
    public ResponseEntity<EuropeanaApiErrorResponse> handleInputValidationError(MissingServletRequestParameterException e, HttpServletRequest httpRequest) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        EuropeanaApiErrorResponse response = (new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled()))
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(responseStatus)
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * Customise the response for {@link org.springframework.web.HttpMediaTypeNotAcceptableException}
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleMediaTypeNotAcceptableException(
            HttpMediaTypeNotAcceptableException e, HttpServletRequest httpRequest) {

        HttpStatus responseStatus = HttpStatus.NOT_ACCEPTABLE;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setError(responseStatus.getReasonPhrase())
                .setMessage("Server could not generate a response that is acceptable by the client")
                .build();

        return ResponseEntity
                .status(responseStatus)
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }


    /**
     * Exception thrown by Spring when RequestBody validation fails.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> handleMethodArgNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpRequest) {
        BindingResult result = e.getBindingResult();
        String error ="";
        List<FieldError> fieldErrors = result.getFieldErrors();
        if(!fieldErrors.isEmpty()) {
            // just return the first error
            error = fieldErrors.get(0).getField() + " " + fieldErrors.get(0).getDefaultMessage();
        }

        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        EuropeanaApiErrorResponse response = new EuropeanaApiErrorResponse.Builder(httpRequest, e, stackTraceEnabled())
                .setStatus(responseStatus.value())
                .setMessage("Invalid request body")
                .setError(error)
                .build();

        return ResponseEntity
                .status(responseStatus)
                .headers(createHttpHeaders(httpRequest))
                .body(response);
    }

    /**
     * Exception throw while handling apikey validation via keycloak
     * @param request request
     * @param ee exception
     * @return EuropeanaApiErrorResponse
     */
    @ExceptionHandler(ApplicationAuthenticationException.class)
    public ResponseEntity<EuropeanaApiErrorResponse> clientRegistrationExceptionHandler(HttpServletRequest request,
                                                                                        ApplicationAuthenticationException ee) {
        if (ee.getResult() != null) {
            KeyValidationResult result = ee.getResult();
            return ResponseEntity.status(result.getHttpStatusCode())
                    .headers(createHttpHeaders(request))
                    .body(new EuropeanaApiErrorResponse.Builder(request, ee, stackTraceEnabled())
                            .setStatus(result.getHttpStatusCode())
                            .setError(result.getValidationError().getError())
                            .setMessage(result.getValidationError().getMessage())
                            .setCode(result.getValidationError().getCode())
                            .build());
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .headers(createHttpHeaders(request))
                    .body( new EuropeanaApiErrorResponse.Builder(request, ee, stackTraceEnabled())
                            .setStatus(HttpServletResponse.SC_UNAUTHORIZED)
                            .setError("Unauthorized")
                            .setMessage(getI18nService()!=null ? getI18nService().getMessage(ee.getI18nKey()):null)
                            .setCode(StringUtils.substringAfter(ee.getI18nKey(), "."))
                            .build());

        }
    }

    protected HttpHeaders createHttpHeaders(HttpServletRequest httpRequest) {
        HttpHeaders headers = new HttpHeaders();
        //enforce application/json as content type, it is the only serialization supported for exceptions
        headers.setContentType(MediaType.APPLICATION_JSON);

        //autogenerate allow header if the service is configured
        if(getRequestPathMethodService()!=null) {
            String allowHeaderValue = getRequestPathMethodService().getMethodsForRequestPattern(httpRequest).orElse(httpRequest.getMethod());
            headers.add(HttpHeaders.ALLOW, allowHeaderValue);
        }
        return headers;
    }

    /**
     * The bean needs to be defined in the individual APIs
     *
     * @return RequestPathMethodService
     */
    AbstractRequestPathMethodService getRequestPathMethodService() {
        return requestPathMethodService;
    }

    protected I18nService getI18nService() {
        return null;
    }
}