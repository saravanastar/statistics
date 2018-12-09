package com.n26.handler;

import com.n26.dto.Transaction;

public interface StatisticsDataHandler {
    public void handle(Transaction transaction);
    public String getType();
}