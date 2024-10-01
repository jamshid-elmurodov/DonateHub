package donatehub.domain.projections;

public interface WithdrawFullStatistic {
    Long getTotalCount();
    Long getPendingCount();
    Long getCompletedCount();
    Long getCanceledCount();
    Long getCompletedAmount();
}