package com.n26.util;

import org.springframework.stereotype.Component;
import com.n26.dto.Statistics;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AppUtil {
    public Statistics buildEmptyData() {
         return Statistics.builder()
                    .avg(new BigDecimal("0"))
                    .sum(new BigDecimal("0"))
                    .count(0)
                    .max(new BigDecimal("0"))
                    .min(new BigDecimal("0"))
                    .historySet(new LinkedList<>())
                    .build();
                    
    }
    
     public LocalDateTime stringToDate(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
