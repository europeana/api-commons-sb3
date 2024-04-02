package eu.europeana.api.commons_sb3.error;

import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Helper class to generate (error) responses
 */
public class ResponseUtils {

    private ResponseUtils() {
        // hide public constructor to prevent initialization
    }

    /**
     * Get a string representation of an exception's stacktrace
     * @param throwable exception
     * @return string representation of the exception's stacktrace
     */
    public static String getExceptionStackTrace(Throwable throwable) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }


    /**
     * Gets the URI path in the request, appending any query parameters
     *
     * @param httpRequest Http request
     * @return Ssring containing request URI and query parameters
     */
    public static String getRequestPath(HttpServletRequest httpRequest) {
        return httpRequest.getQueryString() == null ? String.valueOf(httpRequest.getRequestURL()) :
                        String.valueOf(httpRequest.getRequestURL().append("?").append(httpRequest.getQueryString()));
    }
}
