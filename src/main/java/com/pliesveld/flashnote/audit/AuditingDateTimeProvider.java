package com.pliesveld.flashnote.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Implements a Spring-data
 */

@Component
public class AuditingDateTimeProvider implements DateTimeProvider {

    private final DateTimeService dateTimeService;

    @Autowired
    public AuditingDateTimeProvider(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public Calendar getNow() {
        return GregorianCalendar.from(dateTimeService.getCurrentDateAndTime());
    }
}
