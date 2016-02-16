package com.pliesveld.flashnote.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("housekeepingService")
@Repository
@Transactional
public class HousekeepingServiceImpl implements HousekeepingService {

    /*
    @Value("${newaccount.expiration.days}")
    private int accountExpirationDays;

    @Value("${deck.expiration.days}")
    private int deckExpirationDays;

    @Value("${index.expiration.days}")
    private int indexDeckCollection;
    
    */

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void expiredAccountPurgeJob() {
        // purge newly created accounts that have been activated
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void expiredDeckPurgeJob() {
        // purge decks of accounts that are not subscribed
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void indexDeckCollectionJob() {
        // index document collection for search / card-recommendation functionality

    }
}
