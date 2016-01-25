package com.pliesveld.flashnote.domain;


import java.util.HashMap;
import java.util.Map;


public enum StudentType
{
    USER          (1,       "A simple student",     "Provides basic access for creating and reviewing flashnotes."),
    PREMIUM       (10,      "An A+ student",        "Advanced features for supporting members.  Increased storage; Group management; Non-expiring flashnotes."),
    MODERATOR     (100,     "Class Narc",           "Citizens entrusted with the responsibility to maintain law and order."),
    ADMIN         (200,     "Principle",            "Administrator");

    private int id;
    private String name;
    private String description;

    private final static Map<Integer,StudentType> intToEnum = new HashMap<>();

    StudentType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    static {
        for(StudentType type : values())
        {
            intToEnum.put(type.getId(),type);
        }
    }

    public static StudentType fromInteger(Integer id)
    {
        return intToEnum.get(id);
    }
}
