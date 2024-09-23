package donatehub.service.payment.click;

import donatehub.domain.request.ClickReq;
import donatehub.domain.response.ClickRes;
import donatehub.domain.response.PaymentRes;

public interface ClickService {
    ClickRes prepare(ClickReq clickReq);

    void complete(ClickReq clickReq, Float donationAmount);

    PaymentRes create(Float amount);

    ClickReq readBody(String body);
}
