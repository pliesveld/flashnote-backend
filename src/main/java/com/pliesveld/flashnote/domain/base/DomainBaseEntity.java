package com.pliesveld.flashnote.domain.base;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class DomainBaseEntity implements Serializable {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
