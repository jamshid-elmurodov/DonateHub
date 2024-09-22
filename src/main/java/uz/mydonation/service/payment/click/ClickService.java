package uz.mydonation.service.payment.click;

import uz.mydonation.domain.request.ClickReq;
import uz.mydonation.domain.response.ClickRes;
import uz.mydonation.domain.response.PaymentRes;

public interface ClickService {
    ClickRes prepare(ClickReq clickReq);

    void complete(ClickReq clickReq, Float donationAmount);

    PaymentRes create(Float amount);

    ClickReq readBody(String body);
}
