package n26.statistics.application.representation;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import n26.statistics.domain.Transaction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRepresentation {
    @ApiModelProperty(notes = "The transaction amount")
    Double amount;
    @ApiModelProperty(notes = "The transaction time in epoch in millis in UTC time zone (this is not current\n" +
            "timestamp)")
    Long timestamp;

    public Transaction toDomainObject() {
        return new Transaction(this.amount,this.timestamp);
    }
}
