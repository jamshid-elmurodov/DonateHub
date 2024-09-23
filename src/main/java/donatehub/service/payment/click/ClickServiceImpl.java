package donatehub.service.payment.click;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import donatehub.domain.exception.BaseException;
import donatehub.domain.request.ClickReq;
import donatehub.domain.response.ClickRes;
import donatehub.domain.response.PaymentRes;

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
    public PaymentRes create(Float amount) {
        String id = UUID.randomUUID().toString();

        String url = String.format("https://my.click.uz/services/pay?service_id=%s&merchant_id=%s&amount=%s&transaction_param=%s&return_url=%s", serviceId, merchantId, amount, id, returnUrl);

        return new PaymentRes(id, url);
    }

    @Override
    public ClickReq readBody(String body) {
        try {
            return objectMapper.readValue(body, ClickReq.class);
        } catch (JsonProcessingException e) {
            throw new BaseException(
                    "Click, ma'lumot o'qishda xatolik",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ClickRes prepare(ClickReq clickReq) {
        return null;
    }

    @Override
    public void complete(ClickReq clickReq, Float donationAmount) {
        if (clickReq.getAction() != 1 || !Objects.equals(clickReq.getAmount(), donationAmount)) {
            throw new BaseException(
                    String.format("Donat to'liq amalga oshirilmagan (Click) %s", clickReq.getClickTransId()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
