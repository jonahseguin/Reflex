package com.jonahseguin.reflex.util.obj;

import com.jonahseguin.reflex.Reflex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SlackNotifier {
    private String message;
    private URL url;
    private String displayname;
    private String emojiicon;
    private String destination;

    public boolean sendMessage() {
        if (destination == null) {
            throw new IllegalArgumentException("You can't send a message without specifying an endpoint!");
        }
        if (message == null) {
            throw new IllegalArgumentException("You can't send a message without any text!");
        }
        boolean error = false;
        try {
            String payload = String.format("payload={\"channel\": \"%s\", \"username\" : \"%s\", \"icon_emoji\": \"%s\", \"text\": \"%s\"}", getDestination(), getDisplayname(), getEmojiicon(), getMessage());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(payload.length()));
            OutputStream stream = connection.getOutputStream();
            stream.write(payload.getBytes());
            stream.flush();
            stream.close();
            StringBuilder response = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            connection.disconnect();
            if (connection.getResponseCode() != 200) {
                error = true;
                Reflex.getReflexLogger().error("Received Response Code HTTP " + connection.getResponseCode() + "!\r\nResponse: " + response.toString());
            }
        } catch (IOException e) {
            error = true;
            Reflex.getReflexLogger().error("Could not send message to Slack", e);
        }
        return !error;
    }


    /**
     * @return the message to send to the destination
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message to be sent to the destination.
     *
     * @param message the message to send
     *                See this for documentiation on formatting. https://api.slack.com/docs/formatting
     */
    public void setMessage(String message) {
        this.message = message.replace("\"", "\\\"");
    }

    /**
     * Get the url endpoint for the slack bot
     *
     * @return the URL for the bot
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Set the url endpoint for the slack bot
     *
     * @param url the API endpoint url for the configured integration.
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * Gets name of the bot to be displayed in the channel.
     *
     * @return the bot's displayname
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * Set the display name of the bot to be displayed for channel messages
     *
     * @param displayname the display name
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    /**
     * Returns the bot's emoji icon
     *
     * @return a string representation of the emoji icon
     */
    public String getEmojiicon() {
        return emojiicon;
    }

    /**
     * Sets the bot's icon as an emoji
     *
     * @param emojiicon the string for the emoji, ex :ghost:
     */
    public void setEmojiicon(String emojiicon) {
        this.emojiicon = emojiicon;
    }

    /**
     * The destination for the slack message
     * '@username' for a username, or '#channel' for a channel.
     * ex @zack6849 or #dev
     *
     * @return the destination/
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Set's the destination for the slack message
     * Formatting should be @username for a dm to a user, or #channel to send as a channel message
     *
     * @param destination the destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }
}