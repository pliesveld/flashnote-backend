package com.pliesveld.flashnote.audit;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * Service provides the current date and time.
 */
@Service("dateTimeService")
public class DateTimeServiceImpl implements DateTimeService {
    @Override
    public ZonedDateTime getCurrentDateAndTime() {
        return ZonedDateTime.now();
    }
}
