package donatehub.domain.response;

import java.time.LocalDate;

public interface UserStatisticRes {
    LocalDate getDay();
    Long getCount();
}
