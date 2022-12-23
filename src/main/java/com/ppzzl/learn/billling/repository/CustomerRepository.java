package com.ppzzl.learn.billling.repository;

import com.ppzzl.learn.billling.model.customer.Cust;
import com.ppzzl.learn.billling.model.customer.Serv;
import com.ppzzl.learn.billling.model.customer.ServIdentification;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CustomerRepository {
    private final Map<String, ServIdentification> servIdentifications = new HashMap<>(); // key: accNbr（主业务号码）
    private final Map<Long, Serv> servs = new HashMap<>(); // key: serv.id
    private final Map<Long, Cust> custs = new HashMap<>(); // key: cust.id

    public void addServIdentification(ServIdentification servIdentification) {
        this.servIdentifications.put(servIdentification.getAccNbr(), servIdentification);
    }

    public Optional<ServIdentification> getServIdentification(String accNbr) {
        return Optional.of(this.servIdentifications.get(accNbr));
    }

    public void addServ(Serv serv) {
        this.servs.put(serv.getServId(), serv);
    }

    public Optional<Serv> getServ(long servId) {
        return Optional.of(this.servs.get(servId));
    }

    public void addCust(Cust cust) {
        this.custs.put(cust.getCustId(), cust);
    }

    public Optional<Cust> getCust(long custId) {
        return Optional.of(this.custs.get(custId));
    }
}
