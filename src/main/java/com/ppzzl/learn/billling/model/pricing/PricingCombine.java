package com.ppzzl.learn.billling.model.pricing;

import lombok.Builder;
import lombok.Data;

// 定价组合。
@Data
@Builder
public class PricingCombine {
    private long pricingCombineId;
    private long pricingPlanId;
    private long eventPricingStrategyId;
    private LifeCycle lifeCycle;
    private long pricingObjects;
    private int calcPriority;
}
