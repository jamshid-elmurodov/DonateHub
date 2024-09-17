package uz.mydonation.service.payment;

import uz.mydonation.domain.model.PaymentRes;

public interface PaymentService {
    PaymentRes create(Integer amount, String description);
}
