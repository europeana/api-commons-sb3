# API Commons for Spring Boot v3 - Error module

There are 4 steps to start using the error module in a Spring-Boot 3 API:

1. Make sure you have a class that adds the 2 required beans
```java
    private static final String BEAN_I18N_SERVICE = "i18nService";
    private static final String BEAN_I18N_MESSAGE_SOURCE = "messageSource";

    @Bean(name = BEAN_I18N_MESSAGE_SOURCE)
    public MessageSource i18nMessagesSource(){
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:messages");
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return source;
    }

    @Bean(name = BEAN_I18N_SERVICE)
    public I18nService getI18nService() {
        return new I18nServiceImpl(i18nMessagesSource());
    }
```   

2. Add a file named `messages_en.properties` in your application's resources folder with at least the following content:
```properties
#401
error.operation_not_authorized=Not authorized to do this operation! {0}

# 404
error.not_found=Item not found!

# 400
error.empty_param_mandatory=Empty mandatory parameter!
error.invalid_param_value=Invalid request parameter value! {0}:{1}


# 500
error.server_unexpected_error=An unexpected error occurred on the server!
```
Optionally you can also add to the 401 section
```properties
error.empty_apikey=Empty API key provided!
error.missing_apikey=The API key must be provided in the request!
error.invalid_apikey=Invalid API key! {0}
```
3. Add a GlobalExceptionHandler class, for example
```java
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends EuropeanaGlobalExceptionHandler {
    // exception handling inherited from parent
}
```
This will make sure several basic errors are caught and handled automatically and allows you to define
custom error handling for specific errors if desired.

4. Check if you want to set any of the following error configuration options in your `application.yml` file

  - `server.error.see-also`           optional, default is empty and field won't be displayed
  - `server.error.include-message`    optional, include message field or not, values are from Spring Boot; `never`, `always` or `on_param`, default is `always`
  - `server.error.include-stacktrace` optional, include stacktrace field or not, values are from Spring Boot; `never`, `always` or `on_param`, default is `on_param`
  - `server.error.include-exception`  optional, true or false, default is true
