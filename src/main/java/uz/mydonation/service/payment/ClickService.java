package uz.mydonation.service.payment;

import org.springframework.stereotype.Service;
import uz.mydonation.domain.model.PaymentRes;

@Service(value = "click")
public class ClickService implements PaymentService {
    @Override
    public PaymentRes create(Integer amount, String description) {
        return null;
    }
}
