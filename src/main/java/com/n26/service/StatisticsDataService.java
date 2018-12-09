package com.n26.service;

import com.n26.cache.CacheManager;
import com.n26.constant.AppCache;
import com.n26.constant.StatisticsHandler;
import com.n26.dto.Statistics;
import com.n26.dto.Transaction;
import com.n26.exception.NotParsableDataException;
import com.n26.exception.InvalidStatDataException;
import com.n26.handler.StatisticsDataHandler;
import com.n26.handler.StatisticsDataHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import com.n26.util.AppUtil;

@Service
@Slf4j
public class StatisticsDataService {
    private final StatisticsDataHandlerFactory statisticsDataHandlerFactory;
    private final CacheManager<String, Statistics> statisticsCacheManager;
    private AppUtil appUtil;

    StatisticsDataService(StatisticsDataHandlerFactory statisticsDataHandlerFactory, CacheManager<String, Statistics> statisticsCacheManager,
    AppUtil appUtil) {
        this.statisticsDataHandlerFactory = statisticsDataHandlerFactory;
        this.statisticsCacheManager = statisticsCacheManager;
        this.appUtil = appUtil;
    }

    public void process(Transaction transaction) {
        try {
            if (transaction != null) {
                BigDecimal transAmount = new BigDecimal(transaction.getAmount());
                LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
                LocalDateTime minus1Min = currentTime.minusMinutes(1);
                LocalDateTime transTime = appUtil.stringToDate(transaction.getTimestamp());
                if (transTime.isAfter(currentTime)) {
                    throw new NotParsableDataException("Future Data Exception");
                }
                if (transTime.isAfter(minus1Min) && transTime.isBefore(currentTime)) {
                    transaction.setRequestReceivedTime(currentTime);
                    handleTransaction(transaction);
                } else {
                    throw new InvalidStatDataException("Invalid Time Exception");
                }
            }
        } catch (InvalidStatDataException invalidException) {
            log.error("Invalid Data Exception" , invalidException);
            throw invalidException;
        } catch(java.time.format.DateTimeParseException exception) {
            throw new NotParsableDataException(exception.getMessage());
        } catch (NumberFormatException exception) {
            throw new NotParsableDataException(exception.getMessage());
        } catch(Exception exception) {
            throw exception;
        }
    }

    private void handleTransaction(Transaction transaction) {
        handleNewTransaction(transaction);
        handleRecordTransaction(transaction);
    }

    private void handleNewTransaction(Transaction transaction) {
        StatisticsDataHandler newDataStatisHandler = statisticsDataHandlerFactory.getHandler(StatisticsHandler.NEWDATA_STATISSTICS_DATAHANDLER.toString());
        newDataStatisHandler.handle(transaction);
    }
    private void handleRecordTransaction(Transaction transaction) {
        StatisticsDataHandler refreshDataStatisHandler = statisticsDataHandlerFactory.getHandler(StatisticsHandler.REFRESH_STATISSTICS_DATAHANDLER.toString());
        refreshDataStatisHandler.handle(transaction);
    }

    public Statistics fetchStatData() {
        return statisticsCacheManager.get(AppCache.CURRENT_GENSTAT.toString());
    }

    public void deleteAllTransaction() {
        statisticsCacheManager.add(AppCache.CURRENT_GENSTAT.toString(), null);
    }
}

