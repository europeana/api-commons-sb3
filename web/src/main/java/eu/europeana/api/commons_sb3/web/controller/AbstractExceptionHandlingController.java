package eu.europeana.api.commons_sb3.web.controller;

 import eu.europeana.api.commons_sb3.definitions.web.ApiResponse;
import eu.europeana.api.commons_sb3.definitions.web.ErrorApiResponse;
import eu.europeana.api.commons_sb3.error.HttpException;
import eu.europeana.api.commons_sb3.web.http.HttpHeaders;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractExceptionHandlingController extends ApiResponseBuilder {

    Logger logger = LogManager.getLogger(getClass());
    public static final String PARAM_INCLUDE_ERROR_STACK = "includeErrorStack";
    public static final String PARAM_WSKEY = "wskey";

    final static Map<Class<? extends Exception>, HttpStatus> statusCodeMap = new HashMap<Class<? extends Exception>, HttpStatus>();
    //see DefaultHandlerExceptionResolver.doResolveException
    static {
        statusCodeMap.put(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);
        statusCodeMap.put(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        statusCodeMap.put(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
        statusCodeMap.put(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);
        statusCodeMap.put(ServletRequestBindingException.class, HttpStatus.BAD_REQUEST);
        statusCodeMap.put(ConversionNotSupportedException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        statusCodeMap.put(TypeMismatchException.class, HttpStatus.BAD_REQUEST);
        statusCodeMap.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        statusCodeMap.put(HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        statusCodeMap.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
        statusCodeMap.put(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST);
        statusCodeMap.put(BindException.class, HttpStatus.BAD_REQUEST);
        statusCodeMap.put(NoHandlerFoundException.class, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<String> handleHttpException(HttpException ex, HttpServletRequest req,
                                                      HttpServletResponse response) throws IOException {

        boolean includeErrorStack = Boolean.valueOf(req.getParameter(PARAM_INCLUDE_ERROR_STACK));
        ErrorApiResponse res = getErrorReport(req.getParameter(PARAM_WSKEY), req.getServletPath(),
                ex, includeErrorStack);

        logger.error("Error response sent for http exception: {}", res.getError(), ex);
        return buildErrorResponse(res, ex.getStatus());

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(Exception ex, HttpServletRequest req, HttpServletResponse response) {

        boolean includeErrorStack = Boolean.valueOf(req.getParameter(PARAM_INCLUDE_ERROR_STACK));
        ErrorApiResponse res = getErrorReport(req.getParameter(PARAM_WSKEY), req.getServletPath(),
                ex, includeErrorStack);
        // for runtime exception, log the stacktrace
        logger.error("respond with internal server error for runtime exception: {}", res.getError(), ex);
        return buildErrorResponse(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ServletException.class, NestedRuntimeException.class, BindException.class})
    public ResponseEntity<String> handleMissingRequestParamException(Exception ex, HttpServletRequest req,
                                                                     HttpServletResponse response) {


        boolean includeErrorStack = Boolean.valueOf(req.getParameter(PARAM_INCLUDE_ERROR_STACK));

        HttpStatus statusCode = statusCodeMap.get(ex.getClass());
        if(statusCode == null)
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorApiResponse res = getErrorReport(req.getParameter(PARAM_WSKEY), req.getServletPath(),
                ex, includeErrorStack);

        // for NestedRuntime exception, and known servlet/spring exceptions log the stacktrace as well
        logger.error("respond with internal server error for runtime exception: {}", res.getError(),  ex);
        return buildErrorResponse(res, statusCode);
    }

    protected ResponseEntity<String> buildErrorResponse(ApiResponse res, HttpStatus status) {

        String body = serializeResponse(res);

        MultiValueMap<String, String> headers = buildHeadersMap();

        return new ResponseEntity<String>(body, headers, status);
    }

    protected MultiValueMap<String, String> buildHeadersMap() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>(5);
        headers.add(HttpHeaders.VARY, HttpHeaders.ACCEPT);
        headers.add(HttpHeaders.CONTENT_TYPE, HttpHeaders.CONTENT_TYPE_JSON_UTF8);
        return headers;
    }
}