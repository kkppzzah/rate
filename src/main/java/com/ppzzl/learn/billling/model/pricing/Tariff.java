package com.ppzzl.learn.billling.model.pricing;

import com.ppzzl.learn.billling.model.common.*;
import com.ppzzl.learn.billling.model.product.Product;
import lombok.Builder;
import lombok.Data;

// 资费标准。
@Data
@Builder
public class Tariff {
    private long tariffId;
    private TariffType tariffType;
    private long pricingSectionId;
    private RatableResource resource;
    private Action action;
    private AcctITemType acctITemType;
    private Product subProduct;
    private long tariffUnitId;
    private CalcMethod calcMethod;
    private int rateUnit;
    private RefValue fixedRateValue; // 每次事件，固定收取的费率。
    private RefValue scaledRateValue; // 按照前面定义的计算单元数，收取的每单元的费率。
    private int calcPriority;
    private int belongCycleDuration;
    private ChargeParty chargeParty; //
}
