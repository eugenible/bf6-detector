package ru.eugenible.bf6.detector.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
@Slf4j
public class GameEventNotifier {

    @Value("${secret.bot.token}")
    private String botToken;

    private static final String CHANNEL_ID = "@jenyabf6";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameEventNotifier() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .proxy(ProxySelector.of(new InetSocketAddress("127.0.0.1", 10808)))
                .build();
    }

    /**
     * Sends a text message to a public Telegram channel using native HttpClient.
     *
     * @param message The text content to send
     */
    public void sendMessageToChannel(String message) {
        try {
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

            // Prepare the payload map
            Map<String, String> payload = Map.of(
                    "chat_id", CHANNEL_ID,
                    "text", message
            );

            // Serialize map to JSON string using Jackson
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Build the HTTP Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send synchronously
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Handle errors based on status code
            if (response.statusCode() != 200) {
                throw new RuntimeException("Telegram API Error. Status: " + response.statusCode() + " Body: " + response.body());
            }

            log.info("Сообщение {} отправлено.", message);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status if interrupted

            log.error("Не удалось отправить сообщение.", e);

            throw new RuntimeException("Failed to send Telegram message", e);
        }
    }

}