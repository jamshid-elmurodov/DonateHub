package donatehub.service.payment.click;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import donatehub.domain.exceptions.BaseException;
import donatehub.domain.request.ClickRequest;
import donatehub.domain.response.ClickResponse;
import donatehub.domain.response.PaymentResponse;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClickServiceImpl implements ClickService {
    @Value("${click.service.id}")
    private String serviceId;

    @Value("${click.merchant.id}")
    private String merchantId;

    @Value("${click.return.url}")
    private String returnUrl;

    private final ObjectMapper objectMapper;

    @Override
    public PaymentResponse create(Float amount) {
        String id = UUID.randomUUID().toString();

        String url = String.format("https://my.click.uz/services/pay?service_id=%s&merchant_id=%s&amount=%s&transaction_param=%s&return_url=%s", serviceId, merchantId, amount, id, returnUrl);

        return new PaymentResponse(id, url);
    }

    @Override
    public ClickRequest readBody(String body) {
        try {
            return objectMapper.readValue(body, ClickRequest.class);
        } catch (JsonProcessingException e) {
            throw new BaseException(
                    "Click, ma'lumot o'qishda xatolik",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ClickResponse prepare(ClickRequest clickRequest) {
        return null;
    }

    @Override
    public void complete(ClickRequest clickRequest, Float donationAmount) {
        if (clickRequest.getAction() != 1 || !Objects.equals(clickRequest.getAmount(), donationAmount)) {
            throw new BaseException(
                    String.format("Donat to'liq amalga oshirilmagan (Click) %s", clickRequest.getClickTransId()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
