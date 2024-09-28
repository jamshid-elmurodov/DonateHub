package donatehub.service.payment.mirpay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import donatehub.domain.exceptions.BaseException;
import donatehub.domain.request.MirPayRequest;
import donatehub.domain.response.MirPayResponse;
import donatehub.domain.response.PaymentResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MirPayServiceImpl implements MirPayService {
    private final Logger log;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${mirpay.kassa_id}")
    private String kassaId;

    @Value("${mirpay.api_key}")
    private String apiKey;

    @Override
    public PaymentResponse create(Float amount){
        String token = getToken().token;

        String uri = String.format("https://mirpay.uz/api/create-pay?summa=%s&info_pay=%s", amount, 1001);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                MirPayResponse mirPayResponse = objectMapper.readValue(response.getBody(), MirPayResponse.class);

                return new PaymentResponse(mirPayResponse.getId(), mirPayResponse.getPayinfo().getRedirectUrl());
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

    @Override
    public void complete(MirPayRequest mirPayRequest) {
        log.info("MIRPAY to'lovining tasdiqlash jarayoni boshlangan");

        if (!Objects.equals("Muvaffaqiyatli", mirPayRequest.getStatus())) {
            throw new BaseException(
                    String.format("Donat summasi to'liq amalga oshirilmagan %s", mirPayRequest.getId()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    public MirPayRequest readBody(String body) {
        log.info("MIRPAY to'lov ma'lumotlari o'qilmoqda");

        MirPayRequest mirPayRequest = new MirPayRequest();

        String[] arr = body.split("&");

        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].split("=")[1];
        }

        List<String> list = new ArrayList<>(Arrays.asList(arr));

        mirPayRequest.setId(list.get(0));
        mirPayRequest.setSumma(Float.valueOf(list.get(1)));
        mirPayRequest.setStatus(list.get(2).substring(0, list.get(2).length() - 1));

        return mirPayRequest;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record IToken(String token){}
}
