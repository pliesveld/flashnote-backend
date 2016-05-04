package com.pliesveld.flashnote.domain.base;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class DomainBaseEntity<ID extends Serializable> implements BaseEntity<ID> {

//  Will cause problems if used on hibernate proxy objects, and on circular references.
//  Use with care.
//    @Override
//    final public String toString() {
//        return ReflectionToStringBuilder.toString(this);
//    }


}
