package com.ppzzl.learn.billling.model.pricing;

import lombok.Builder;
import lombok.Data;

// 事件定价策略。
@Data
@Builder
public class EventPricingStrategy {
    private long eventPricingStrategyId;
    private long eventTypeId; // 计费帐务事件类型的唯一标识。
    private String eventPricingStrategyName;
    private String eventPricingStrategyDesc;
}
