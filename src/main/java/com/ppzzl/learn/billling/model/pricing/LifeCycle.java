package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.DateEffType;
import com.ppzzl.learn.billling.model.common.MainState;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

// 生命周期。
@Data
@Builder
public class LifeCycle {
    private long lifeCycleId;
    private MainState state;
    private DateEffType effType;
    private Date effDate;
    private Date expDate;
}
