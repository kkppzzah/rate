package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.RefValueType;
import com.ppzzl.learn.billling.model.common.ValueType;
import lombok.Builder;
import lombok.Data;

// 参考值。
@Data
@Builder
public class RefValue {
    private long refValueId;
    private RefValueType refValueType; // 参考值类型：引用数值、引用定价参考对象、引用参数、为空等。
    private ValueType valueType; // 数值类型：浮点数、整数、字符串、布尔型、日期等。
    private long pricingRefObjectId; // 若参考值类型时引用定价参考对象，该项有效，表示引用定价参考对象标识，PricingRefObject外键。
    private String valueString; // 如果参考值类型是引用数值，该项有效，表示被引用的数值串。
    private long pricingParamId; // 若参考值类型是引用参数，该项有效，表示定价参数，PricingParam外键。
    private int ratePrecision; // 费率单位的精度。
    private int calcPrecision; // 最终计算结果的精度。
}
