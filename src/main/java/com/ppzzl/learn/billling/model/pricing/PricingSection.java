package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.PricingSectionType;
import com.ppzzl.learn.billling.model.common.SectionCalcType;
import lombok.Builder;
import lombok.Data;

// 定价段落。
@Data
@Builder
public class PricingSection {
    private long pricingSectionId;
    private PricingSectionType sectionType;
    private SectionCalcType sectionCalcType;
    private String pricingSectionName;
    private Long parentSectionId;
    private Long pricingRefObjectId; // 进行段落比较时，如果时引用参考对象类型，则本字段指定引用的参考对象标识。
    private int cycleSectionBegin; // 循环段起始值。
    private int cycleSectionDuration; // 循环段间隔。
    private RefValue startRefValue; // 段落起始值取值
    private RefValue endRefValue; // 段落终止值取值
    private boolean judgeResult;
    private long eventPricingStrategyId;
    private int calcPriority; // 计算优先级。
}
