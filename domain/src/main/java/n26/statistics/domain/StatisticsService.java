package n26.statistics.domain;

import java.time.LocalDateTime;

public interface StatisticsService {

    void addTransaction(Transaction transaction);
    Statistic getStatistics(LocalDateTime dateTime);
}
