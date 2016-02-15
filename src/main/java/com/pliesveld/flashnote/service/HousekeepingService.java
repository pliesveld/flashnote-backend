package com.pliesveld.flashnote.service;

/**
 * Created by happs on 1/29/16.
 */
public interface HousekeepingService {
    public void expiredAccountPurgeJob();
    public void expiredDeckPurgeJob();
    public void indexDeckCollectionJob();
}
