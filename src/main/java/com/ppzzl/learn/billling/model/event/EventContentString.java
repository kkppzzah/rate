package com.ppzzl.learn.billling.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventContentString extends EventContent {
    private String value;

    @Override
    public Object getValue() {
        return value;
    }
}
