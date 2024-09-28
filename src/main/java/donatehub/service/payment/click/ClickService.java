package donatehub.service.payment.click;

import donatehub.domain.request.ClickRequest;
import donatehub.domain.response.ClickResponse;
import donatehub.domain.response.PaymentResponse;

public interface ClickService {
    ClickResponse prepare(ClickRequest clickRequest);

    void complete(ClickRequest clickRequest, Float donationAmount);

    PaymentResponse create(Float amount);

    ClickRequest readBody(String body);
}
