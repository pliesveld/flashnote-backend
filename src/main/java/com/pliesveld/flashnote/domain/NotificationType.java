package com.pliesveld.flashnote.domain;

import java.util.HashMap;
import java.util.Map;

public enum NotificationType
{
    SYSTEM_ERROR       (1),
    SYSTEM_INFO        (5),

    USER_ERROR         (50),
    USER_INFO          (55),

    USER_REQUEST       (59);


    private final int id;

    private final static Map<Integer,NotificationType> intToEnum = new HashMap<>();

    static {
        for (NotificationType type : values())
        {
            intToEnum.put(type.getId(),type);
        }
    }

    NotificationType(int id) { this.id = id; }

    public int getId() {
        return id;
    }

    public static NotificationType fromInteger(Integer id)
    {
        return intToEnum.get(id);
    }
}
