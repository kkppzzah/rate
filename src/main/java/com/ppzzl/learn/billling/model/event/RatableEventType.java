package com.ppzzl.learn.billling.model.event;

import com.ppzzl.learn.billling.model.common.MainState;
import com.ppzzl.learn.billling.model.common.SumEventType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

// 计费帐务事件类型。
@Data
@Builder
public class RatableEventType {
    public final static long EVENT_TYPE_ID_LONG_DISTANCE_CALL = 110000001;
    public final static long EVENT_TYPE_ID_LOCAL_CALL = 110000002;
    public final static long EVENT_TYPE_ID_INITIAL_INSTALLATION = 120000003;
    private long eventTypeId;
    private String name;
    private SumEventType sumEventType; // 计费事件类型归类。
    private String eventTypeCode; // 计费事件类型编码。
    private MainState state;
    private Date effDate;
    private Date expDate;
}
