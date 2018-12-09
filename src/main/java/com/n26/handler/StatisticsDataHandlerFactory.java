package com.n26.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StatisticsDataHandlerFactory {
    Map<String, StatisticsDataHandler> dataHandlerMap;

    StatisticsDataHandlerFactory(ApplicationContext context) {
        dataHandlerMap = new HashMap<>();
        Map<String, StatisticsDataHandler> stringStatisticsDataHandlerMap = context.getBeansOfType(StatisticsDataHandler.class);
        stringStatisticsDataHandlerMap.entrySet().stream().forEach(entry -> {
            StatisticsDataHandler statisticsDataHandler = entry.getValue();
            dataHandlerMap.put(statisticsDataHandler.getType(), statisticsDataHandler);
        });
    }

    public StatisticsDataHandler getHandler(String handlerName) {
        return dataHandlerMap.get(handlerName);
    }
}

