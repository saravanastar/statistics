package com.n26.handler;

import com.n26.cache.CacheManager;
import com.n26.constant.AppCache;
import com.n26.constant.StatisticsHandler;
import com.n26.dto.Statistics;
import com.n26.dto.Transaction;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import com.n26.util.AppUtil;

@Component
public class NewStatisticsDataHandler implements StatisticsDataHandler {
    private final CacheManager<String, Statistics> cacheManager;
    private final AppUtil appUtil;

    NewStatisticsDataHandler(CacheManager cacheManager, AppUtil appUtil) {
        this.cacheManager = cacheManager;
        this.appUtil = appUtil;
  
    }

    public void handle(Transaction transaction) {
        Statistics statistics = cacheManager.get(AppCache.CURRENT_GENSTAT.toString());
        if (statistics == null) {
            statistics = appUtil.buildEmptyData();
        }
        long count = statistics.getCount();
        BigDecimal sum = statistics.getSum();
        LinkedList<BigDecimal> historyTransaction = statistics.getHistorySet();

        historyTransaction.add(new BigDecimal(transaction.getAmount()));
        Collections.sort(historyTransaction);
        sum = sum.add(new BigDecimal(transaction.getAmount()));
        count = count + 1;
        double avg = sum.doubleValue()/historyTransaction.size();
        statistics.setAvg(new BigDecimal(avg));
        statistics.setMax(historyTransaction.peekLast());
        statistics.setMin(historyTransaction.peekFirst());
        statistics.setSum(sum);
        statistics.setCount(count);
        statistics.setHistorySet(historyTransaction);

        cacheManager.add(AppCache.CURRENT_GENSTAT.toString(), statistics);
    }

    @Override
    public String getType() {
        return StatisticsHandler.NEWDATA_STATISSTICS_DATAHANDLER.toString();
    }
}

