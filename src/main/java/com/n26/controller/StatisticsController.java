package com.n26.controller;

import com.n26.dto.Statistics;
import com.n26.dto.Transaction;
import com.n26.exception.InvalidStatDataException;
import com.n26.service.StatisticsDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import com.n26.util.AppUtil;

@RestController
public class StatisticsController {
    private final StatisticsDataService statisticsDataService;
    private final AppUtil appUtil;

    StatisticsController(StatisticsDataService statisticsDataService, AppUtil appUtil) {
        this.statisticsDataService = statisticsDataService;
         this.appUtil = appUtil;
    }

    @GetMapping(value = "/statistics")
    public ResponseEntity<Statistics> getTransactionStatistics() {
        Statistics statistics = statisticsDataService.fetchStatData();
        if (statistics == null) {
            statistics = appUtil.buildEmptyData();
        }
        return ResponseEntity.ok(statistics);
    }

    @PostMapping(value = "/transactions")
    public ResponseEntity postTransactionData(@Valid @RequestBody Transaction transaction) {
        statisticsDataService.process(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/transactions/list")
    public ResponseEntity postTransactionData(@Valid @RequestBody List<Transaction> transaction) throws InvalidStatDataException {
        transaction.stream().forEach(transaction1 -> {
                statisticsDataService.process(transaction1);
        });
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/transactions")
    public ResponseEntity deleteTransactionData() {
        statisticsDataService.deleteAllTransaction();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

