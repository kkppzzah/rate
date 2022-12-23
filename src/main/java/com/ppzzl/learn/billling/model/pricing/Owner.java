package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.ChargePartyFlag;
import com.ppzzl.learn.billling.model.common.ObjectType;
import lombok.Builder;
import lombok.Data;

// 属主
@Data
@Builder
public class Owner {
    private long ownerId;
    private ObjectType ownerObjectType;
    private long ownerObjectId; // 当需要指定定价计划中包含的特定对象时，使用该字段。对应PricingObjects。
    private ChargePartyFlag chargePartyFlag;
}
