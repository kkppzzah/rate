package com.ppzzl.learn.billling.model.event;

import com.ppzzl.learn.billling.model.common.AcctITemType;
import com.ppzzl.learn.billling.model.product.Product;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

// 批价后帐务事件。
@Data
@Builder
public class RatedEvent {
    private AcctITemType acctITemType;
    private Product product;
    private BigDecimal fee;
    private String ratePath;
    private String discountPath;
}
