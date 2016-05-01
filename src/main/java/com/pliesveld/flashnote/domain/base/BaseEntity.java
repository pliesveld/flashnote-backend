package com.pliesveld.flashnote.domain.base;

import java.io.Serializable;

public interface BaseEntity<ID extends Serializable> {
    ID getId();
}
