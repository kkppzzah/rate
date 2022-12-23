package com.ppzzl.learn.billling.rate;

import com.ppzzl.learn.billling.model.common.RefValueType;
import com.ppzzl.learn.billling.model.common.ValueType;
import com.ppzzl.learn.billling.model.pricing.RefValue;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class RefValueResolver {
    public Object resolve(RefValue refValue) {
        if (refValue == null) {
            return null;
        }
        RefValueType refValueType = refValue.getRefValueType();
        ValueType valueType = refValue.getValueType();
        if (refValueType.equals(RefValueType.CONSTANT)) {
            // 时间类型。
            if (valueType.equals(ValueType.TIME)) {
                return LocalTime.parse(refValue.getValueString());
            }
            // 浮点类型。
            if (valueType.equals(ValueType.DOUBLE)) {
                return Double.valueOf(refValue.getValueString());
            }
        }
        return null;
    }
}
