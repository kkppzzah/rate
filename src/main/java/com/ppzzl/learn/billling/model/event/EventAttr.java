package com.ppzzl.learn.billling.model.event;

import com.ppzzl.learn.billling.model.common.DomainDataType;
import com.ppzzl.learn.billling.model.common.DomainEventAttrType;

import java.util.HashMap;
import java.util.Map;

// 事件属性。
public enum EventAttr {
    CALLING_NUMBER(100001, DomainEventAttrType.SIMPLE, DomainDataType.STRING, 11, 0, "CALLING_NUMBER", "主叫号码"),
    CALLED_NUMBER(100002, DomainEventAttrType.SIMPLE, DomainDataType.STRING, 11, 0, "CALLED_NUMBER", "被叫号码"),
    START_TIME(100003, DomainEventAttrType.SIMPLE, DomainDataType.DATETIME, 11, 0, "START_TIME", "起始时间"),
    DURATION(100004, DomainEventAttrType.SIMPLE, DomainDataType.INTEGER, 11, 0, "DURATION", "时长"),;

    private static final Map<Long, EventAttr> eventAttrs = new HashMap<>();
    static {
        eventAttrs.put(CALLING_NUMBER.eventAttrId, CALLING_NUMBER);
        eventAttrs.put(CALLED_NUMBER.eventAttrId, CALLED_NUMBER);
        eventAttrs.put(START_TIME.eventAttrId, START_TIME);
        eventAttrs.put(DURATION.eventAttrId, DURATION);
    }

    public static  EventAttr from(long eventAttrId) {
        return eventAttrs.get(eventAttrId);
    }

    private final long eventAttrId;
    private final DomainEventAttrType eventAttrType;
    private final DomainDataType dataType;
    private final int length;
    private final int precision;
    private final String chName;
    private final String enName;

    public long getEventAttrId() {
        return this.eventAttrId;
    }

    EventAttr(long eventAttrId, DomainEventAttrType eventAttrType, DomainDataType dataType,
              int length, int precision, String chName, String enName) {
        this.eventAttrId = eventAttrId;
        this.eventAttrType = eventAttrType;
        this.dataType = dataType;
        this.length = length;
        this.precision = precision;
        this.chName = chName;
        this.enName = enName;
    }
}
