package com.ppzzl.learn.billling.model.customer;

import com.ppzzl.learn.billling.model.common.CustType;
import com.ppzzl.learn.billling.model.common.MainState;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

// 客户
@Data
@Builder
public class Cust {
    private long custId;
    private String custName;
    private CustType custType;
    private boolean isVip;
    private Cust parent;
    private String custCode;
    private MainState state;
    private Date effDate;
    private Date expDate;
}
