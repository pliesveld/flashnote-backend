package com.pliesveld.flashnote.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for all service-layer classes.  This class is used to mark the
 * package that should be scanned with @ComponentScan
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface FlashNoteService {
}
