package com.ppzzl.learn.billling.rate;

import com.ppzzl.learn.billling.model.customer.Cust;
import com.ppzzl.learn.billling.model.customer.Serv;
import com.ppzzl.learn.billling.model.customer.ServIdentification;
import com.ppzzl.learn.billling.model.event.Event;
import com.ppzzl.learn.billling.model.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Context {
    private Event event;
    private ServIdentification servIdentification;
    private Product product;
    private Serv serv;
    private Cust cust;
}
