package com.ppzzl.learn.billling.repository;

import com.ppzzl.learn.billling.model.event.RatableEventType;
import org.apache.commons.collections.map.MultiKeyMap;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventRepository {
    private final MultiKeyMap ratableEventTypes = new MultiKeyMap();

    public void addRatableEventType(RatableEventType ratableEventType) {
        this.ratableEventTypes.put(
                ratableEventType.getEventTypeId(), ratableEventType.getEventTypeCode(), ratableEventType
        );
    }

    public Optional<RatableEventType> getRatableEventType(String eventTypeCode) {
        return Optional.of((RatableEventType)this.ratableEventTypes.get(eventTypeCode));
    }
}
