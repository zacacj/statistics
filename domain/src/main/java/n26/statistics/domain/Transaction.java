package n26.statistics.domain;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class Transaction {
    Double amount;
    LocalDateTime localDateTime;

    public Transaction(Double amount, long timestamp) {
        this.amount = amount;
        this.localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"));
    }
}
