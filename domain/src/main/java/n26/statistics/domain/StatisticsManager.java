package n26.statistics.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticsManager implements StatisticsService {

    Map<Integer, Statistic> seconds;

    public StatisticsManager() {
        seconds = new ConcurrentHashMap<>(60);
    }

    @Override
    public void addTransaction(Transaction transaction) {

        if (transaction.getLocalDateTime().isBefore(LocalDateTime.now().minusMinutes(1)))
            throw new TooOldTransaction();

        Integer second = transaction.localDateTime.getSecond();
        Statistic statistic = seconds.get(second);
        Statistic newStatistic;
        if (statistic == null)
            newStatistic = new Statistic(transaction.amount,
                    transaction.amount,
                    1L,
                    transaction.amount,
                    transaction.amount,
                    transaction.getLocalDateTime());
        else
            newStatistic = statistic.cloneWithTransaction(transaction);
        seconds.put(transaction.localDateTime.getSecond(), newStatistic);
    }

    @Override
    public Statistic getStatistics(LocalDateTime dateTime) {
        clearOlderStatistics(dateTime);
        return seconds.values().stream().reduce(
                new Statistic(0.0, 0.0, 0L, Double.MIN_VALUE, Double.MAX_VALUE, LocalDateTime.now()),
                (a, b) -> a.accumulateStatistic(b)
        );
    }

    private void clearOlderStatistics(LocalDateTime dateTime) {
        seconds.forEach((key, value) -> {
            if (value.getMinute().isBefore(dateTime.minusMinutes(1)))
                seconds.put(key, new Statistic(0.0, 0.0, 0L, Double.MIN_VALUE, Double.MAX_VALUE, LocalDateTime.now()));
        });
    }
}
