package com.pliesveld.flashnote.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Transactional(readOnly = true)
public interface HousekeepingService {
    @Transactional
    public void expiredAccountPurgeJob();

    @Transactional
    public void expiredDeckPurgeJob();

    @Transactional
    public void indexDeckCollectionJob();
}
