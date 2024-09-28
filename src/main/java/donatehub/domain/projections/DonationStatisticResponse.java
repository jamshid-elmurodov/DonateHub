package donatehub.domain.projections;

import java.time.LocalDate;

public interface DonationStatisticResponse {
    LocalDate getDay();
    Integer getAmount();
}
