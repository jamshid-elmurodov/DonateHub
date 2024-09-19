package uz.mydonation.service.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.mydonation.domain.response.PaymentRes;

@Service(value = "click")
public class ClickService implements PaymentService {
    @Value("${click.service.id}")
    private String serviceId;

    @Value("${click.merchant.id}")
    private String merchantId;

    @Override
    public PaymentRes create(Integer amount, String description) {
        String url = String.format("https://my.click.uz/services/pay?service_id=%s&merchant_id=%s&amount=%d&transaction_param=%d&return_url=%s", serviceId, merchantId, amount, 1, "");

        return new PaymentRes();
    }
}
