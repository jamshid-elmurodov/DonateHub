package uz.mydonation.service.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.mydonation.domain.exception.BaseException;
import uz.mydonation.domain.response.PaymentRes;

@Service(value = "mirpay")
@RequiredArgsConstructor
public class MirPayService implements PaymentService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${payment.kassa_id}")
    private String kassaId;

    @Value("${payment.api_key}")
    private String apiKey;

    public PaymentRes create(Integer amount, String description){
        String token = getToken().token;

        String uri = String.format("https://mirpay.uz/api/create-pay?summa=%s&info_pay=%s", amount, description);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return objectMapper.readValue(response.getBody(), PaymentRes.class);
            }

            throw new BaseException(
                    "Error working with payment (" + response.getBody() + ")",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (BaseException e){
            throw e;
        } catch (Exception e) {
            throw new BaseException("Error working with mirpay", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private IToken getToken(){
        String uri = String.format("https://mirpay.uz/api/connect?kassaid=%s&api_key=%s", kassaId, apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return objectMapper.readValue(response.getBody(), IToken.class);
            }


            throw new BaseException(
                    String.format("Token olishda xatolik yuz berdi ( %s ) ", response.getBody()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (BaseException e){
            throw e;
        } catch (Exception e){
            throw new BaseException("Error mirpay token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record IToken(String token){}
}
