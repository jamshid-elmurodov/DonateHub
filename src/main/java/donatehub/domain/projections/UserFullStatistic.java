package donatehub.domain.projections;

public interface UserFullStatistic {
    Long getTotalCount();
    Long getEnableTotalCount();
    Long getDailyTotalCount();
    Long getDailyEnableTotalCount();
}
