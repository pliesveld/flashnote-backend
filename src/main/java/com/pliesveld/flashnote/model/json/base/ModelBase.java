package com.pliesveld.flashnote.model.json.base;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class ModelBase implements Serializable {


    private static final long serialVersionUID = -4445464005269416512L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
