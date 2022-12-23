package com.ppzzl.learn.billling.model.customer;

import com.ppzzl.learn.billling.model.common.ServState;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Serv {
    private long servId;
    private long agreementId;
    private long custId;
    private long productId;
    private Date createDate;
    private ServState state;
    private Date stateDate;
}
