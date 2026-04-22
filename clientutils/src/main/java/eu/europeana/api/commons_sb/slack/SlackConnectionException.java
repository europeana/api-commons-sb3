package eu.europeana.api.commons_sb.slack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SlackConnectionException extends RuntimeException {

    public static final String SLACK_WEBHOOK_NOT_CONFIGURED = "Slack webhook not configured, No status Report will be sent to slack";
    public static final String SLACK_ERROR                  = "Exception occurred while sending slack message !! ";

    private static final long serialVersionUID = 8496589899526050812L;

    private String error;

    public SlackConnectionException(String message) {
        super(message);
    }

    @JsonCreator
    public SlackConnectionException(@JsonProperty("error") String error
            , @JsonProperty("error_description") String message) {
        super(message);
        this.error = error;
    }

    public SlackConnectionException(String message, Throwable th){
        super(message, th);
    }

    public String getError() {
        return this.error;
    }
}
