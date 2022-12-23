package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.CycleType;
import com.ppzzl.learn.billling.model.common.MeasureMethod;
import com.ppzzl.learn.billling.model.common.OwnerPropertyType;
import lombok.Builder;
import lombok.Data;

// 定价参考对象。
@Data
@Builder
public class PricingRefObject {
    private long pricingRefObjectId;
    private long ownerId; // 属主标识。
    private OwnerPropertyType propertyType; // 属主属性类型。如金钱类、积量类、事件类、外部类等。
    private long propertyDefineId; // 属主属性标识。说明引用哪个属性，外键引用属性定义表。
    private String externPropertyString; // 定价参考队形的外部属性标识。
    private MeasureMethod measureMethod; // 独立方法标识。
    private CycleType historyTimeType; // 定价参考对象时间类型，如帐期、月份等，例如用于表达前两个月的费用总额。
    private int historyTimeDuration; // 定价参考对象时间偏移量，如向前第3个帐期，则取值=-3。
    private int historyTimeQuantity; // 定价参考对象所属的周期时间对象。
}
