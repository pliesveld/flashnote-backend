package com.pliesveld.flashnote.domain;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public enum AccountRole
{
    ROLE_ACCOUNT   (1,           "Student applicant",    "Limited access to services.  Cannot create new resources."),
    ROLE_USER          (10,      "A simple student",     "Provides basic access for creating and reviewing flashnotes."),
    ROLE_PREMIUM       (50,      "An A+ student",        "Advanced features for supporting members.  Increased storage; Group management; Non-expiring flashnotes."),
    ROLE_MODERATOR     (100,     "Class Narc",           "Citizens entrusted with the responsibility to maintain law and order."),
    ROLE_ADMIN         (200,     "Principle",            "Administrator");

    final private int id;
    final private String title;
    final private String description;

    private final static Map<Integer,AccountRole> intToEnum = new HashMap<>();

    AccountRole(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    static {
        for(AccountRole type : values())
        {
            intToEnum.put(type.getId(),type);
        }
    }

    public static AccountRole fromInteger(Integer id)
    {
        return intToEnum.get(id);
    }

    public static Collection<AccountRole> allRoles()
    {
        return intToEnum.values();
    }

    @Override
    final public String toString() {
        return super.toString();
    }
}
