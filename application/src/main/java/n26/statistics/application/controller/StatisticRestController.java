package n26.statistics.application.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import n26.statistics.application.representation.StatisticRepresentation;
import n26.statistics.application.representation.TransactionRepresentation;
import n26.statistics.domain.Statistic;
import n26.statistics.domain.StatisticsService;
import n26.statistics.domain.Transaction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class StatisticRestController {

    private StatisticsService statisticsService;

    public StatisticRestController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @ApiOperation(value = "Add a new transaction to statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added"),
            @ApiResponse(code = 404, message = "Might be due to trying to add transactions older than 1 min")
    })
    @PostMapping("/transaction")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addTransaction(@RequestBody TransactionRepresentation representation) {
        statisticsService.addTransaction(representation.toDomainObject());
    }

    @ApiOperation(value = "Get statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Statistics successfully retrieved"),
    })
    @GetMapping("/statistics")
    public HttpEntity<StatisticRepresentation> getStatistics() {
        return new HttpEntity<StatisticRepresentation>(new StatisticRepresentation(statisticsService.getStatistics(LocalDateTime.now())));
    }
}
