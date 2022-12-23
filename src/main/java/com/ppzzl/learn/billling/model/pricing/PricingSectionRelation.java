package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.RelationType;
import lombok.Builder;
import lombok.Data;

// 定价段落关系。
@Data
@Builder
public class PricingSectionRelation {
    private PricingSection aSection;
    private PricingSection bSection;
    private RelationType relationType;
}
