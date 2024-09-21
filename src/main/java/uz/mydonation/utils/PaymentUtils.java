package uz.mydonation.utils;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uz.mydonation.domain.response.MirPayCompilationRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class PaymentUtils {
    private final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");

    public MirPayCompilationRes readBodyMirpay(String body) {
        log.info("MIRPAY to'lov ma'lumotlari o'qilmoqda");

        MirPayCompilationRes mirPayCompilationRes = new MirPayCompilationRes();

        String[] arr = body.split("&");

        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].split("=")[1];
        }

        List<String> list = new ArrayList<>(Arrays.asList(arr));

        mirPayCompilationRes.setId(list.get(0));
        mirPayCompilationRes.setSumma(list.get(1));
        mirPayCompilationRes.setStatus(list.get(2).substring(0, list.get(2).length() - 1));

        return mirPayCompilationRes;
    }
}
