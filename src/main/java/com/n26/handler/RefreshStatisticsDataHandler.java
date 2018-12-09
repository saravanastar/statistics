package com.n26.handler;

import com.n26.cache.CacheManager;
import com.n26.constant.AppCache;
import com.n26.constant.StatisticsHandler;
import com.n26.dto.Statistics;
import com.n26.dto.Transaction;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.time.format.DateTimeFormatter;
import com.n26.util.AppUtil;

@Component
public class RefreshStatisticsDataHandler implements StatisticsDataHandler {
    private final CacheManager<String, Statistics> cacheManager;
    private final AppUtil appUtil;
    
    RefreshStatisticsDataHandler(CacheManager cacheManager, AppUtil appUtil) {
        this.cacheManager = cacheManager;
        this.appUtil = appUtil;
      
    }

    @Override
    public void handle(Transaction transaction) {
        if (transaction != null) {
            LocalDateTime currentTime = transaction.getRequestReceivedTime();
            LocalDateTime minusOneMin = currentTime.minusSeconds(60);
            LocalDateTime transactionTime = appUtil.stringToDate(transaction.getTimestamp());

            if (transactionTime.isBefore(currentTime) && transactionTime.isAfter(minusOneMin)) {
                Duration duration = Duration.between(transactionTime, minusOneMin);
                long seconds = Math.abs(duration.getSeconds());

                CompletableFuture.runAsync(() -> {
                    ScheduledExecutorService exec = Executors.newScheduledThreadPool(10);
                    exec.schedule(new Runnable() {
                        @Override
                        public void run() {
                            removeOldStatisticsData(transaction);
                        }
                    }, seconds, TimeUnit.SECONDS);
                });
            }

        }
    }

    /**
     *
     * @param transaction
     */
    public synchronized  void removeOldStatisticsData(Transaction transaction) {
        Statistics statistics = cacheManager.get(AppCache.CURRENT_GENSTAT.toString());
        BigDecimal transactionAmount = new BigDecimal(transaction.getAmount());

        LinkedList<BigDecimal> historyTransaction = statistics.getHistorySet();

        if (!historyTransaction.isEmpty()) {
            historyTransaction.remove(transactionAmount);
        }
        if (historyTransaction.isEmpty()) {
            statistics.setSum(new BigDecimal("0"));
            statistics.setAvg(new BigDecimal("0"));
            statistics.setMax(new BigDecimal("0"));
            statistics.setMin(new BigDecimal("0"));
            statistics.setCount(0);
        } else {
            double sum = historyTransaction.stream().mapToDouble(BigDecimal::doubleValue).sum();
           double avg = sum / historyTransaction.size();
            Collections.sort(historyTransaction);
            statistics.setMax(historyTransaction.peekLast());
            statistics.setMin(historyTransaction.peekFirst());
            statistics.setCount(historyTransaction.size());
            statistics.setSum(new BigDecimal(sum));
            statistics.setAvg(new BigDecimal(avg));
        }
        statistics.setHistorySet(historyTransaction);
        cacheManager.add(AppCache.CURRENT_GENSTAT.toString(), statistics);
    }

    @Override
    public String getType() {
        return StatisticsHandler.REFRESH_STATISSTICS_DATAHANDLER.toString();
    }
}

