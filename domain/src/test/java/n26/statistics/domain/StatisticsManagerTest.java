package n26.statistics.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class StatisticsManagerTest {


    StatisticsService statisticsService;

    @Before
    public void setup() {
        statisticsService = new StatisticsManager();
    }

    @Test
    public void addTransactionFirstTime() {

        LocalDateTime datetime1 = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch1 = datetime1.atZone(zoneId).toInstant().toEpochMilli();
        Transaction transaction = new Transaction(20.2, epoch1);
        statisticsService.addTransaction(transaction);
        Statistic statistic = statisticsService.getStatistics(LocalDateTime.now());

        Assert.assertEquals(transaction.getAmount(), statistic.getSum());
        Assert.assertEquals(transaction.getAmount(), statistic.getAvg());
        Assert.assertEquals(transaction.getAmount(), statistic.getMax());
        Assert.assertEquals(transaction.getAmount(), statistic.getMin());
        Assert.assertEquals(new Long(1), statistic.getCount());
    }

    @Test
    public void addSecondTransactionForSameSecond() {
        LocalDateTime datetime1 = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch1 = datetime1.atZone(zoneId).toInstant().toEpochMilli();

        Transaction transaction1 = new Transaction(20.2, epoch1);
        statisticsService.addTransaction(transaction1);

        Transaction transaction2 = new Transaction(10.2, epoch1);
        statisticsService.addTransaction(transaction2);

        Statistic statistic = statisticsService.getStatistics(LocalDateTime.now());

        Double sum = new Double(transaction1.getAmount() + transaction2.getAmount());
        Double avg = new Double(sum / 2);
        Assert.assertEquals(sum, statistic.getSum());
        Assert.assertEquals(avg, statistic.getAvg());
        Assert.assertEquals(transaction1.getAmount(), statistic.getMax());
        Assert.assertEquals(transaction2.getAmount(), statistic.getMin());
        Assert.assertEquals(new Long(2), statistic.getCount());
    }

    @Test
    public void addSecondTransactionForDifferentSeconds() {
        LocalDateTime datetime1 = LocalDateTime.now();
        LocalDateTime datetime2 = datetime1.minusSeconds(2);
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch1 = datetime1.atZone(zoneId).toInstant().toEpochMilli();
        long epoch2 = datetime2.atZone(zoneId).toInstant().toEpochMilli();
        Transaction transaction1 = new Transaction(20.2, epoch1);
        Transaction transaction2 = new Transaction(10.2, epoch2);
        statisticsService.addTransaction(transaction1);
        statisticsService.addTransaction(transaction2);

        Statistic statistic = statisticsService.getStatistics(LocalDateTime.now());

        Double sum = new Double(transaction1.getAmount() + transaction2.getAmount());
        Double avg = new Double(sum / 2);
        Assert.assertEquals(sum, statistic.getSum());
        Assert.assertEquals(avg, statistic.getAvg());
        Assert.assertEquals(transaction1.getAmount(), statistic.getMax());
        Assert.assertEquals(transaction2.getAmount(), statistic.getMin());
        Assert.assertEquals(new Long(2), statistic.getCount());
    }

    @Test(expected = TooOldTransaction.class)
    public void addTransactionOlderThanAMinute() {
        LocalDateTime datetime1 = LocalDateTime.now();
        LocalDateTime datetime2 = datetime1.minusMinutes(2);
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch1 = datetime1.atZone(zoneId).toInstant().toEpochMilli();
        long epoch2 = datetime2.atZone(zoneId).toInstant().toEpochMilli();
        Transaction transaction1 = new Transaction(20.2, epoch1);
        Transaction transaction2 = new Transaction(10.2, epoch2);
        statisticsService.addTransaction(transaction1);
        statisticsService.addTransaction(transaction2);
    }

    @Test
    public void addTransactionForASecondWhenMinuteChanged() {
        LocalDateTime datetime1 = LocalDateTime.now();
        LocalDateTime datetime2 = datetime1.minusMinutes(1);
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch1 = datetime1.atZone(zoneId).toInstant().toEpochMilli();
        long epoch2 = datetime2.atZone(zoneId).toInstant().toEpochMilli();
        Transaction transaction1 = new Transaction(20.2, epoch1);
        Transaction transaction2 = new Transaction(10.2, epoch2);
        statisticsService.addTransaction(transaction2);

        Statistic statistic = statisticsService.getStatistics(LocalDateTime.now());

        Assert.assertEquals(transaction2.getAmount(), statistic.getSum());
        Assert.assertEquals(transaction2.getAmount(), statistic.getAvg());
        Assert.assertEquals(transaction2.getAmount(), statistic.getMax());
        Assert.assertEquals(transaction2.getAmount(), statistic.getMin());
        Assert.assertEquals(new Long(1), statistic.getCount());

        statisticsService.addTransaction(transaction1);

        statistic = statisticsService.getStatistics(LocalDateTime.now());

        Assert.assertEquals(transaction1.getAmount(), statistic.getSum());
        Assert.assertEquals(transaction1.getAmount(), statistic.getAvg());
        Assert.assertEquals(transaction1.getAmount(), statistic.getMax());
        Assert.assertEquals(transaction1.getAmount(), statistic.getMin());
        Assert.assertEquals(new Long(1), statistic.getCount());
    }

    @Test
    public void getStatisticsWhenNoTransaction() {
        Statistic statistic = statisticsService.getStatistics(LocalDateTime.now());

        Assert.assertEquals(new Double(0.0), statistic.getSum());
        Assert.assertEquals(new Double(0.0), statistic.getAvg());
        Assert.assertEquals(new Long(0), statistic.getCount());
    }

    @Test
    public void getStatisticsWithTransactionsInMultipleSecondsWhenMinuteChanged() throws InterruptedException {

        LocalDateTime datetime1 = LocalDateTime.now();
        LocalDateTime datetime2 = datetime1.minusSeconds(58);
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch1 = datetime1.atZone(zoneId).toInstant().toEpochMilli();
        long epoch2 = datetime2.atZone(zoneId).toInstant().toEpochMilli();
        Transaction transaction2 = new Transaction(10.2, epoch2);
        statisticsService.addTransaction(transaction2);

        Thread.sleep(2000);
        Statistic statistic = statisticsService.getStatistics(datetime2);

        Transaction transaction1 = new Transaction(20.2, epoch1);

        Assert.assertEquals(transaction2.getAmount(), statistic.getSum());
        Assert.assertEquals(transaction2.getAmount(), statistic.getAvg());
        Assert.assertEquals(transaction2.getAmount(), statistic.getMax());
        Assert.assertEquals(transaction2.getAmount(), statistic.getMin());
        Assert.assertEquals(new Long(1), statistic.getCount());

        statisticsService.addTransaction(transaction1);

        statistic = statisticsService.getStatistics(LocalDateTime.now());

        Assert.assertEquals(transaction1.getAmount(), statistic.getSum());
        Assert.assertEquals(transaction1.getAmount(), statistic.getAvg());
        Assert.assertEquals(transaction1.getAmount(), statistic.getMax());
        Assert.assertEquals(transaction1.getAmount(), statistic.getMin());
        Assert.assertEquals(new Long(1), statistic.getCount());
    }
}
