package donatehub.utils;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Setter
public class TelegramLogAppender extends AppenderBase<ILoggingEvent> {
    private final RestTemplate restTemplate = new RestTemplate();
    private String botToken;
    private String chatId;

    @Async
    @Override
    protected void append(ILoggingEvent eventObject) {
        String logMessage = eventObject.getFormattedMessage();
        String logLevel = eventObject.getLevel().toString();

        String prefix = switch (logLevel) {
            case "ERROR" -> "❗️[ERROR] \n";
            case "WARN" -> "⚠️[WARN] \n";
            case "INFO" -> "ℹ️[INFO] \n";
            default -> "[LOG] ";
        };

        String formattedMessage = prefix + logMessage;

        String telegramApiUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", formattedMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        restTemplate.postForObject(telegramApiUrl, request, String.class);
    }
}

