package eu.europeana.api.commons_sb3.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *  This class collates all the HTTP methods that are implemented for all unique request
 *  patterns within a Spring Boot application.
 *  This is useful for populating the HTTP Allow header, when generating API responses.
 *
 *  To use:
 *   Either
 *    1. extend this class within your project;
 *    2. create a bean in your project
 *      @Bean
 *      public ApiRequestPathMethodService getApiRequestPathMethodService(){
 *         return new ApiRequestPathMethodService();
 *     }
 *     later the class can be used to createAllowHeader via
 *             requestMethodService.getMethodsForRequestPattern(request);
 *   For more reference: User Set API
 * */
@Component
public class ApiRequestPathMethodService implements ApplicationListener<ContextRefreshedEvent> {

    /**
    * Map request urls to Http request methods (implemented across the application) with the url
    * pattern.
    */
    private final Map<String, Set<String>> requestPathMethodMap = new HashMap<>();

    /**
     * Populate request url pattern - request methods map
     * */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping
                .getHandlerMethods();

        for (RequestMappingInfo info : handlerMethods.keySet()) {
            PathPatternsRequestCondition p = info.getPathPatternsCondition();

            // get all request methods for this pattern
            final Set<String> requestMethods = info.getMethodsCondition().getMethods().stream()
                    .map(Enum::toString)
                    .collect(Collectors.toSet());

            if (p != null) {
                p.getPatterns().forEach(pattern ->
                    addToMap(requestPathMethodMap, pattern.getPatternString(), requestMethods));
            }
        }
    }

    /**
    * Gets request methods that are implemented across the application for this request's URL
    * pattern. The return value from this method is used when setting the Allow header in API
    * responses.
    *
    * @param request {@link HttpServletRequest} instance
    * @return Optional containing matching request methods, or empty optional if no match could be
    *     determined.
    */
    public Optional<String> getMethodsForRequestPattern(HttpServletRequest request) {
        Object patternAttribute = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        if (patternAttribute == null) {
            return Optional.empty();
        }

        Set<String> requestMethods = requestPathMethodMap.get(patternAttribute.toString());
        if (requestMethods==null) {
            return Optional.empty();
        }

        String methods = String.join(",", requestMethods);
        return Optional.of(methods);
    }

    /** This method adds url patterns and their matching request methods to the map. */
    private void addToMap(Map<String, Set<String>> map, String urlPattern, Set<String> requestMethods) {
        map.computeIfAbsent(urlPattern, k -> requestMethods);
        // Each pattern can be used across multiple request handlers, so we append here.
        Set<String> existing = map.get(urlPattern);
        existing.addAll(requestMethods);
    }
}
