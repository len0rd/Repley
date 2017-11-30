package com.leotech.monitor.model.sql;

import java.sql.Types;

//TODO: This is super lame. I tried using a generic, but couldnt quite get it?? Visit again.
public class Tuple {
    private String key;
    private Object value;
    private int type;

    public Tuple (String key, Object value, int type) {
        this.key    = key;
        this.value  = value;
        this.type   = type;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public int getBasicSqlType() {
        return type;
    }
}
