package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.ObjectType;
import lombok.Builder;
import lombok.Data;

// 定价包含的对象。
@Data
@Builder
public class PricingObjects {
    private long pricingObjectsId;
    private long pricingPlanId;
    private ObjectType objectType;
    private long objectId;
    private int objectAmountStart;
    private int objectAmountEnd;
}
