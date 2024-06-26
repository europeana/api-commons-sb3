package eu.europeana.api.commons_sb3.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
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
 *   - extend this class within your project;
 *   - instantiate the subclass with the Spring WebApplicationContext;
 *   - pass the instance as an argument to BaseRestController.createAllowHeader()
 * */
public abstract class AbstractRequestPathMethodService implements InitializingBean {

    /**
    * Map request urls to Http request methods (implemented across the application) with the url
    * pattern.
    */
    private final Map<String, Set<String>> requestPathMethodMap = new HashMap<>();

    protected final WebApplicationContext applicationContext;

    protected AbstractRequestPathMethodService(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
    * For Services or APIs using spring boot version > 2.7.x
    * Spring Boot 2.7 no longer defines MVC’s main requestMappingHandlerMapping bean as @Primary bean.
    * In the unlikely event that we are injecting RequestMappingHandlerMapping bean, we need to be specific
    * about the bean name.
    * We need the bean of name - "requestMappingHandlerMapping"
    *
    * @see <a href="https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes#spring-mvcs-requestmappinghandlermapping-is-no-longer-primary">Dcoumentation</a>
    *
    * Otherwise we get -   org.springframework.beans.factory.NoUniqueBeanDefinitionException:
    * No qualifying bean of type 'org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping'
    * available: expected single matching bean but found 2: requestMappingHandlerMapping,controllerEndpointHandlerMapping
    *
    * Populate request url pattern - request methods map */
    @Override
    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        for (RequestMappingInfo info : handlerMethods.keySet()) {
            PatternsRequestCondition p = info.getPatternsCondition();

            // get all request methods for this pattern
            final Set<String> requestMethods = info.getMethodsCondition().getMethods().stream()
                  .map(Enum::toString)
                  .collect(Collectors.toSet());

            if (p != null) {
                for (String url : p.getPatterns()) {
                    addToMap(requestPathMethodMap, url, requestMethods);
                }
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
