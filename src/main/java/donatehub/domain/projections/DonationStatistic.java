package donatehub.domain.projections;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public interface DonationStatistic {
    @JsonFormat(pattern = "dd/MM")
    LocalDate getDay();

    Integer getAmount();
}
