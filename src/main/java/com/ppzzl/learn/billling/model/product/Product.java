package com.ppzzl.learn.billling.model.product;

import com.ppzzl.learn.billling.model.common.BillingMode;
import com.ppzzl.learn.billling.model.common.MainState;
import com.ppzzl.learn.billling.model.common.ProductClassification;
import com.ppzzl.learn.billling.model.common.ProductType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

// 产品。
@Data
@Builder
public class Product {
    private long productId;
    private BillingMode billingMode;
    private long pricingPlanId;
    private String productName;
    private String productDescription;
    private ProductType productType;
    private ProductClassification productClassification;
    private String productCode;
    private MainState state;
    private Date effDate;
    private Date expDate;
}
