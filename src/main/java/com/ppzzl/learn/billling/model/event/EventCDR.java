package com.ppzzl.learn.billling.model.event;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventCDR extends Event {
    public EventCDR(long eventId, long sourceEventType, CDR cdr) {
        super(eventId, sourceEventType);
        this.cdr = cdr;
    }

    @Data
    @Builder
    public static class CDR {
        private String callingNumber;
        private String calledNumber;
        private Date startTime;
        private int duration;
    }
    private static interface Visitor {
        EventContent visit(CDR cdr);
    }
    private static final Map<EventAttr, Visitor> visitors = new HashMap<>();
    static {
        visitors.put(EventAttr.CALLING_NUMBER, cdr1 -> new EventContentString(cdr1.getCallingNumber()));
        visitors.put(EventAttr.CALLED_NUMBER, cdr1 -> new EventContentString(cdr1.getCalledNumber()));
        visitors.put(EventAttr.START_TIME, cdr1 -> new EventContentDate(cdr1.getStartTime()));
        visitors.put(EventAttr.DURATION, cdr1 -> new EventContentInteger(cdr1.getDuration()));
    }

    private final CDR cdr;

    @Override
    public EventContent getEventContent(EventAttr eventAttr) {
        Visitor visitor = visitors.get(eventAttr);
        if (visitor != null) {
            return visitor.visit(this.cdr);
        }
        return null;
    }
}
