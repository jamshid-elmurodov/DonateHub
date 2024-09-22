package uz.mydonation.service.payment.mirpay;

import uz.mydonation.domain.request.MirPayReq;
import uz.mydonation.domain.response.PaymentRes;

public interface MirPayService {
    PaymentRes create(Float amount);

    void complete(MirPayReq mirPayReq);

    MirPayReq readBody(String body);
}
