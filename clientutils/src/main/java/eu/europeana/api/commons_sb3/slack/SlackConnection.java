package eu.europeana.api.commons_sb3.slack;

import eu.europeana.api.commons_sb3.http.HttpConnection;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.Collections;

/**
 * Represents a connection to a Slack workspace using a webhook URL.
 * This class allows sending messages to a configured Slack channel.
 * @author srishti singh
 * @since 26 March 2026
 */
public class SlackConnection {

    private String slackWebhook;

    public SlackConnection(String slackWebhook) {
        this.slackWebhook = slackWebhook;
    }

    /**
     * Publishes a status report message to a Slack channel using a configured webhook URL.
     * If the webhook URL is not set or is blank, the method does nothing.
     *
     * @param message The status report message to be sent to the Slack channel.
     * @throws IOException If an I/O error occurs during the HTTP connection or message transmission.
     */
    public  void publishStatusReport(String message) throws SlackConnectionException {
        if (StringUtils.isBlank(slackWebhook)) {
            throw new SlackConnectionException(SlackConnectionException.SLACK_WEBHOOK_NOT_CONFIGURED);
        }

        HttpConnection httpConnection = new HttpConnection(true);
        try (CloseableHttpResponse response = httpConnection.post(
                     slackWebhook,
                     message,
                     Collections.singletonMap(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8"),null)) {
                if (response.getCode() != HttpStatus.SC_OK) {
                    throw new SlackConnectionException(SlackConnectionException.SLACK_ERROR, EntityUtils.toString(response.getEntity()));
                }
        } catch (IOException | ParseException e) {
            throw new SlackConnectionException(SlackConnectionException.SLACK_ERROR + e.getMessage(), e);
        } finally {
            try {
                httpConnection.close();
            } catch (IOException e) {
                throw new SlackConnectionException("Exception while closing the connection "+ e.getMessage(), e);
            }
        }
    }
}