package com.ppzzl.learn.billling.model.common;

public enum AcctITemType {
    LONG_DISTANCE_CALL("200001", "长途话费"),
    LONG_DISTANCE_CALL_EXTRA("200002", "长途附加费"),
    LOCAL_CALL("100002", "本地话费"),
    INITIAL_INSTALLATION("300001", "初装费");

    private final String code;
    private final String name;

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    AcctITemType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
