package donatehub.utils;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import donatehub.domain.entities.DonationEntity;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotExecutor {
    private final RestTemplate restTemplate;
    @Value("${bot.token}")
    private String token;

    public void sendDonationInfo(Long chatId, DonationEntity donation){
        String json = new Gson().toJson(new Message(chatId, donationToString(donation)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        String url = String.format("https://api.telegram.org/bot%s/sendMessage", token);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Donation info sent successfully.");
        } else {
            log.error("Failed to send donation info. Status code: {}", response.getStatusCode());
        }
    }

    public String donationToString(DonationEntity donation){
        return "🆕Yangi donat" +
                "\nIsm: " + donation.getDonaterName() +
                "\nXabar: " + donation.getMessage() +
                "\nMiqdor: " + donation.getPayment().getAmount();
    }

    private record Message(Long chat_id, String text) {}
}
