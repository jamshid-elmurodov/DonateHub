package uz.mydonation.service.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.mydonation.domain.response.PaymentRes;

import java.util.UUID;

@Service(value = "click")
public class ClickService implements PaymentService {
    @Value("${click.service.id}")
    private String serviceId;

    @Value("${click.merchant.id}")
    private String merchantId;

    @Value("${click.return.url}")
    private String returnUrl;

    @Override
    public PaymentRes create(Integer amount) {
        String id = UUID.randomUUID().toString();

        String url = String.format("https://my.click.uz/services/pay?service_id=%s&merchant_id=%s&amount=%d&transaction_param=%s&return_url=%s", serviceId, merchantId, amount, id, returnUrl);

        return new PaymentRes(id, new PaymentRes.Info(url));
    }
}
