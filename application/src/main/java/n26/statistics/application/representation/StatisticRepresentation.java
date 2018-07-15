package n26.statistics.application.representation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import n26.statistics.domain.Statistic;

@NoArgsConstructor
@Getter
@Setter
public class StatisticRepresentation {
    @ApiModelProperty(notes = "The total sum of transaction value in the last 60 seconds")
    Double sum;
    @ApiModelProperty(notes = "The average amount of transaction value in the last 60\n" +
            "seconds")
    Double avg;
    @ApiModelProperty(notes = "The single highest transaction value in the last 60 seconds")
    Double max;
    @ApiModelProperty(notes = "The single lowest transaction value in the last 60 seconds")
    Double min;
    @ApiModelProperty(notes = "The total number of transactions happened in the last 60\n" +
            "seconds")
    Long count;

    public StatisticRepresentation(Statistic statistic){
        this.sum = statistic.getSum();
        this.avg = statistic.getAvg();
        this.max = statistic.getMax();
        this.min = statistic.getMin();
        this.count = statistic.getCount();
    }
}
