package com.ppzzl.learn.billling.model.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

// 源事件。
@AllArgsConstructor
public class Event {
    @Getter
    private long eventId;
    @Getter
    private long sourceEventType; // 唯一标识一种源事件类型，如语音市话跳表事件、语音计时事件、互联网使用事件、互联星空使用事件等

    public EventContent getEventContent(EventAttr eventAttr) {
        return null;
    }
}
