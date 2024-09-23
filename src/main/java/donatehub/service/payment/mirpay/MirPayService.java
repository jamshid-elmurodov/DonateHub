package donatehub.service.payment.mirpay;

import donatehub.domain.request.MirPayReq;
import donatehub.domain.response.PaymentRes;

public interface MirPayService {
    PaymentRes create(Float amount);

    void complete(MirPayReq mirPayReq);

    MirPayReq readBody(String body);
}
