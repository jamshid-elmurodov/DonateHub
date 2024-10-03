package donatehub.domain.projections;

import java.time.LocalDate;

public interface AdminStatisticByGraphic {
    LocalDate getDate();
    Integer getWithdraws();
    Integer getDonations();
    Integer getUsers();
}