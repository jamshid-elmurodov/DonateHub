package donatehub.domain.projections;

public interface DonationFullStatistic {
    Long getTotalCount();
    Float getTotalAmount();
    Long getDailyCount();
    Float getDailyAmount();
}