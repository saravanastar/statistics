package com.n26.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import com.n26.serializer.BigDecimalSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;

@Data
@Builder
public class Statistics implements Serializable {
    @JsonIgnore
    LinkedList<BigDecimal> historySet;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal sum;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal avg;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal max;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal min;
    private long count;
}

