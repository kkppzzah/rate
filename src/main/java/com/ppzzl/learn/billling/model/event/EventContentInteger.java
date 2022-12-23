package com.ppzzl.learn.billling.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventContentInteger extends EventContent {
    private int value;

    @Override
    public Object getValue() {
        return value;
    }
}
