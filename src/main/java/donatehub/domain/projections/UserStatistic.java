package donatehub.domain.projections;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public interface UserStatistic {
    @JsonFormat(pattern = "dd/MM")
    LocalDate getDay();
    Long getCount();
}
