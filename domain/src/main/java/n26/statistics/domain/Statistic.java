package n26.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Statistic {
    private Double sum;
    private Double avg;
    private Long count;
    private Double max;
    private Double min;
    private LocalDateTime minute;

    public Statistic cloneWithTransaction(Transaction transaction) {
        if (transaction.getLocalDateTime().isAfter(minute))
            return new Statistic(transaction.amount,
                    transaction.amount,
                    1L,
                    transaction.amount,
                    transaction.amount,
                    transaction.getLocalDateTime());
        else
            return incrementStatistic(transaction);
    }

    private Statistic incrementStatistic(Transaction transaction) {
        Double newSum = sum + transaction.amount;
        Long newCount = new Long(count + 1L);
        Double newAvg = newSum / newCount;
        Double newMax = Double.max(max, transaction.amount);
        Double newMin = Double.min(min,transaction.amount);
        return new Statistic(newSum,
                newAvg,
                newCount,
                newMax,
                newMin,
                minute);
    }

    public Statistic accumulateStatistic(Statistic b) {
        Double newSum = sum + b.sum;
        Long newCount = count + b.count;
        Double newAvg = newSum / newCount;
        Double newMax = Double.max(max, b.max);
        Double newMin = Double.min(min,b.min);
        return new Statistic(newSum,
                newAvg,
                newCount,
                newMax,
                newMin,
                minute);    }
}
