package com.ppzzl.learn.billling.model.customer;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

// 主产品标识。
@Data
@Builder
public class ServIdentification {
    private long servId;
    private String accNbr; // 主业务号码。
    private Date effDate;
    private Date expDate;
}
