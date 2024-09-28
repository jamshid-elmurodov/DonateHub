package donatehub.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public interface UserStatisticRes {
    @JsonFormat(pattern = "dd.MM")
    LocalDate getDay();
    Long getCount();
}
