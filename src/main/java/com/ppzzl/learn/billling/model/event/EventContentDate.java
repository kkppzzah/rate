package com.ppzzl.learn.billling.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EventContentDate extends EventContent {
    private Date value;

    @Override
    public Object getValue() {
        return value;
    }
}
