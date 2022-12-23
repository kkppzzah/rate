package com.ppzzl.learn.billling.model.pricing;

import lombok.Builder;
import lombok.Data;

// 定价计划。
@Data
@Builder
public class PricingPlan {
    private long pricingPlanId;
    private String pricingPlanName;
    private String pricingDesc;
    private String paramDesc;
}
