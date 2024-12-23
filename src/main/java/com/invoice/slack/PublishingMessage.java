package com.invoice.slack;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.IOException;

public class PublishingMessage {

    @Autowired
    private Environment env;


    /**
     * Post a message to a channel your app is in using ID and message text
     */
    /**
     * Post a message to a channel your app is in using ID and message text
     */
    public void publishMessage(String message) {
        // you can get this instance via ctx.client() in a Bolt app
        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");
        try {
            // Call the chat.postMessage method using the built-in WebClient
            var result = client.chatPostMessage(r -> r
                            // The token you used to initialize your app
                            .token(env.getProperty("slack-app-token"))
                            .channel(env.getProperty("slack-app-channel-id"))
                            .text(message)
                    // You could also use a blocks[] array to send richer content
            );
            // Print result, which includes information about the message (like TS)
            logger.info("result {}", result);
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
    }
}
