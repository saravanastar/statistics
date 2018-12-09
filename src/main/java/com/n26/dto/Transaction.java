package com.n26.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction implements Serializable {
    @NotNull
    private String amount;
    @NotNull
    private String timestamp;
    LocalDateTime requestReceivedTime;

}