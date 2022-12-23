package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.MeasureMethod;
import lombok.Builder;
import lombok.Data;

// 资费单位描述。
@Data
@Builder
public class TariffUnit {
    private long tariffUnitId;
    private MeasureMethod measureMethod; // 该资费单位对应的度量方法，如在长话计费时，秒、分对应的度量方法都是时长
    private String tariffUnitName; // 名称，具体说明该资费单位含义。
    private double standardConversionRate; // 标准换算比率。只葱度量方法的标准单位换算到本单位的比率，比如度量方法为时长，标准单位为秒。
    // 如果本单位为分钟，则换算比率为60。
}
