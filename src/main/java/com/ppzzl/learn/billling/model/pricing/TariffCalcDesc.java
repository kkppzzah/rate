package com.ppzzl.learn.billling.model.pricing;

import lombok.Builder;
import lombok.Data;

// 资费计算方法描述。
@Data
@Builder
public class TariffCalcDesc {
    private long tariffCalcId;
    private String tariffCalcName;
}
