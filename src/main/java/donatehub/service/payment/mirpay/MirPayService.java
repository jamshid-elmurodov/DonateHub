package donatehub.service.payment.mirpay;

import donatehub.domain.request.MirPayRequest;
import donatehub.domain.response.PaymentResponse;

public interface MirPayService {
    PaymentResponse create(Float amount);

    void complete(MirPayRequest mirPayRequest);

    MirPayRequest readBody(String body);
}
